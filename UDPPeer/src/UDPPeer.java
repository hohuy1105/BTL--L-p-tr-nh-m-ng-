import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class UDPPeer extends JFrame {

    // --- Biến giao diện ---
    private JTextArea logArea;
    private JTextField remoteIpField, remotePortField, localPortField;
    private JFileChooser fileChooser;
    private JLabel statusLabel, savePathLabel;
    private JButton listenButton, sendButton, browseButton;
    private JProgressBar currentFileProgress, totalProgress;
    private JLabel progressLabel;
    private JLabel speedEtrLabel;

    // --- Biến logic ---
    private DatagramSocket socket;
    private boolean isListening = false;
    private final User currentUser;
    private final AuthService authService;
    private final HistoryService historyService;
    private File saveDirectory;
    private List<File> filesToSend = new ArrayList<>();

    // --- Biến MỚI cho tính toán tốc độ/ETR ---
    private Timer transferStatsTimer;
    private final AtomicLong bytesTransferredInSecond = new AtomicLong(0);
    private long totalBytesForCurrentTransfer;
    private final AtomicLong totalBytesSent = new AtomicLong(0);


    // --- Biến giao diện Lịch sử ---
    private JTable historyTable;
    private DefaultTableModel historyTableModel;
    private final DateTimeFormatter tableDateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");

    // --- Biến Icon ---
    private ImageIcon iconPlay, iconStop, iconSend, iconFolder, iconUsers, iconConnected, iconDisconnected, iconLogout;

    public UDPPeer(User user, AuthService authService, HistoryService historyService) {
        super("UDP Peer File Transfer");
        this.currentUser = user;
        this.authService = authService;
        this.historyService = historyService;
        this.saveDirectory = FileSystemView.getFileSystemView().getDefaultDirectory();

        loadIcons();
        initComponents();
        setupDragAndDrop();
        setupTransferStatsTimer();
        loadInitialHistory();
        updateStatus(false);
    }

    private void initComponents() {
        setTitle("UDP File Transfer - " + currentUser.getUsername());
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
    
        add(createTopControlPanel(), BorderLayout.NORTH);
    
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Nhật ký hoạt động"));
        String[] columnNames = {"Hành động", "Tên tệp", "Dung lượng", "Đối tác (Peer)", "Thời gian"};
        historyTableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        historyTable = new JTable(historyTableModel);
        historyTable.setFillsViewportHeight(true);
        historyTable.setRowHeight(24);
        setupHistoryTableContextMenu();
        JScrollPane historyScrollPane = new JScrollPane(historyTable);
        historyScrollPane.setBorder(BorderFactory.createTitledBorder("Lịch sử Giao dịch"));
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, historyScrollPane, logScrollPane);
        mainSplitPane.setResizeWeight(0.6);
        add(mainSplitPane, BorderLayout.CENTER);
        
        add(createBottomPanel(), BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    
    // --- Các hàm MỚI và Cập nhật ---
    
    private void setupTransferStatsTimer() {
        transferStatsTimer = new Timer(1000, e -> {
            long bytesInSecond = bytesTransferredInSecond.getAndSet(0);
            if (bytesInSecond == 0 && transferStatsTimer.isRunning()) {
                return;
            }
            
            String speed = humanReadableByteCountSI(bytesInSecond) + "/s";
            
            long remainingBytes = totalBytesForCurrentTransfer - totalBytesSent.get();
            String etr = "∞";

            if (bytesInSecond > 0 && remainingBytes > 0) {
                long secondsRemaining = remainingBytes / bytesInSecond;
                etr = String.format("%02d:%02d:%02d", secondsRemaining / 3600, (secondsRemaining % 3600) / 60, secondsRemaining % 60);
            } else if (remainingBytes <= 0) {
                 etr = "Hoàn tất";
            }
            
            speedEtrLabel.setText("Tốc độ: " + speed + " - Thời gian còn lại: " + etr);
        });
    }

    private void setupHistoryTableContextMenu() {
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem openFileItem = new JMenuItem("Mở File");
        JMenuItem openFolderItem = new JMenuItem("Hiển thị trong Thư mục");

        openFileItem.addActionListener(e -> {
            int selectedRow = historyTable.getSelectedRow();
            if (selectedRow >= 0) {
                handleContextMenuAction(selectedRow, true);
            }
        });
        
        openFolderItem.addActionListener(e -> {
            int selectedRow = historyTable.getSelectedRow();
            if (selectedRow >= 0) {
                handleContextMenuAction(selectedRow, false);
            }
        });

        contextMenu.add(openFileItem);
        contextMenu.add(openFolderItem);

        historyTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) showMenu(e);
            }
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) showMenu(e);
            }
            private void showMenu(MouseEvent e) {
                int row = historyTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    historyTable.setRowSelectionInterval(row, row);
                    // Chỉ bật menu cho các file đã "Nhận"
                    boolean isReceived = "Nhận".equals(historyTableModel.getValueAt(row, 0));
                    openFileItem.setEnabled(isReceived);
                    openFolderItem.setEnabled(isReceived);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void handleContextMenuAction(int row, boolean openFile) {
        String direction = (String) historyTableModel.getValueAt(row, 0);
        if (!"Nhận".equals(direction)) {
            JOptionPane.showMessageDialog(this, "Chức năng này chỉ dành cho các tệp đã nhận.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String fileName = (String) historyTableModel.getValueAt(row, 1);
        File file = new File(saveDirectory, fileName);

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "Tệp không còn tồn tại tại đường dẫn: " + file.getAbsolutePath(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (openFile) {
                Desktop.getDesktop().open(file);
            } else {
                Desktop.getDesktop().open(file.getParentFile());
            }
        } catch (IOException ex) {
            log("Lỗi khi mở tệp/thư mục: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Không thể thực hiện hành động.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Hàm startSendingFiles được cập nhật để khởi động và dừng timer
    private void startSendingFiles() {
        if (!isListening) {
            JOptionPane.showMessageDialog(this, "Vui lòng 'Bắt đầu Lắng nghe' trước khi gửi file!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (filesToSend.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tệp/thư mục để gửi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Reset và khởi động timer với biến đếm mới
        totalBytesForCurrentTransfer = filesToSend.stream().mapToLong(File::length).sum();
        totalBytesSent.set(0); // <-- Reset biến đếm
        bytesTransferredInSecond.set(0);
        transferStatsTimer.start();
        
        new Thread(this::sendFiles).start();
    }
    
    // Hàm sendFiles được cập nhật để dừng timer và tăng số byte đã truyền
    private void sendFiles() {
        try {
            InetAddress address = InetAddress.getByName(remoteIpField.getText());
            int port = Integer.parseInt(remotePortField.getText());
            sendPacket(PacketType.TOTAL_SIZE_HEADER.getHeader() + totalBytesForCurrentTransfer, address, port);
            
            for (File file : filesToSend) {
                sendPacket(PacketType.FILE_HEADER.getHeader() + file.getName() + "::" + file.length(), address, port);
                log("Đang gửi: " + file.getName() + " đến " + address.getHostAddress());
                progressLabel.setText("Đang gửi: " + file.getName());
                
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    long fileBytesSent = 0;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        socket.send(new DatagramPacket(buffer, bytesRead, address, port));
                        
                        // Cập nhật các biến đếm
                        bytesTransferredInSecond.addAndGet(bytesRead);
                        fileBytesSent += bytesRead;
                        long currentTotalSent = totalBytesSent.addAndGet(bytesRead); // Lấy giá trị tổng mới nhất
                        
                        // Cập nhật thanh tiến trình
                        updateProgress(fileBytesSent, file.length(), currentTotalSent, totalBytesForCurrentTransfer);
                        
                        Thread.sleep(1);
                    }
                }
                
                sendPacket(PacketType.FILE_EOF.getHeader() + file.getName(), address, port);
                historyService.addHistory(new HistoryEntry(currentUser.getUsername(), "Gửi", file.getName(), address.getHostAddress() + ":" + port, LocalDateTime.now(), file.length()));
            }

            sendPacket(PacketType.TRANSMISSION_EOF.getHeader(), address, port);
            log("Hoàn tất gửi tất cả các tệp.");
            progressLabel.setText("Hoàn tất!");
            refreshHistoryTable();
            filesToSend.clear();
        } catch (Exception e) {
            log("Lỗi nghiêm trọng khi gửi tệp: " + e.getMessage());
            e.printStackTrace();
        } finally {
            transferStatsTimer.stop();
            speedEtrLabel.setText("");
        }
    }
    
    // Hàm startListening được cập nhật để khởi động và dừng timer
    private void startListening() {
        try {
            int port = Integer.parseInt(localPortField.getText());
            socket = new DatagramSocket(port);
            updateStatus(true);
            log("Bắt đầu lắng nghe trên cổng " + port);

            Thread listener = new Thread(() -> {
                long totalBytesToReceive = 0;
                long totalBytesReceived = 0;
                long currentFileBytesReceived = 0;
                long currentFileSize = 0;
                
                FileOutputStream fos = null;
                File receivingFile = null;
                try {
                    byte[] buffer = new byte[65507];
                    while (isListening) {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String message = new String(packet.getData(), 0, packet.getLength());
                        PacketType type = PacketType.fromMessage(message);
                        if (type != null) {
                             switch (type) {
                                case TOTAL_SIZE_HEADER:
                                    totalBytesToReceive = Long.parseLong(message.substring(type.getHeader().length()));
                                    totalBytesReceived = 0;
                                    bytesTransferredInSecond.set(0);
                                    transferStatsTimer.start();
                                    updateProgress(0, 0, 0, 0);
                                    break;
                                case FILE_HEADER:
                                    if (fos != null) fos.close();
                                    String[] parts = message.substring(type.getHeader().length()).split("::");
                                    String fileName = parts[0];
                                    currentFileSize = Long.parseLong(parts[1]);
                                    currentFileBytesReceived = 0;
                                    receivingFile = new File(saveDirectory, fileName);
                                    fos = new FileOutputStream(receivingFile);
                                    log("Đang nhận tệp: " + fileName + " từ " + packet.getAddress().getHostAddress());
                                    progressLabel.setText("Đang nhận: " + fileName);
                                    break;
                                case FILE_EOF:
                                    if (fos != null) {
                                        fos.close();
                                        fos = null;
                                        log("Đã nhận xong tệp: " + receivingFile.getName());
                                        historyService.addHistory(new HistoryEntry(currentUser.getUsername(), "Nhận", receivingFile.getName(), packet.getAddress().getHostAddress() + ":" + packet.getPort(), LocalDateTime.now(), receivingFile.length()));
                                        refreshHistoryTable();
                                    }
                                    break;
                                case TRANSMISSION_EOF:
                                    transferStatsTimer.stop();
                                    speedEtrLabel.setText("");
                                    log("Đã nhận tất cả các tệp.");
                                    progressLabel.setText("Hoàn tất!");
                                    updateProgress(100, 100, 100, 100);
                                    break;
                            }
                        } else { 
                            if (fos != null) {
                                int bytesRead = packet.getLength();
                                fos.write(packet.getData(), 0, bytesRead);
                                bytesTransferredInSecond.addAndGet(bytesRead);
                                currentFileBytesReceived += bytesRead;
                                totalBytesReceived += bytesRead; 

                                updateProgress(currentFileBytesReceived, currentFileSize, totalBytesReceived, totalBytesToReceive);
                            }
                        }
                    }
                } catch (SocketException se) {
                    if (isListening) log("Socket đã đóng.");
                } catch (Exception e) {
                    log("Lỗi khi nhận dữ liệu: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    try { if (fos != null) fos.close(); } catch (Exception ex) { ex.printStackTrace(); }
                    if (transferStatsTimer.isRunning()) transferStatsTimer.stop();
                }
            });
            listener.setDaemon(true);
            listener.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể lắng nghe: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            updateStatus(false);
        }
    }

    private void updateProgress(long currentFileBytes, long currentFileSize, long totalBytes, long totalSize) {
        SwingUtilities.invokeLater(() -> {
            int fileProg = (currentFileSize == 0) ? 0 : (int) (currentFileBytes * 100 / currentFileSize);
            int totalProg = (totalSize == 0) ? 0 : (int) (totalBytes * 100 / totalSize);
            currentFileProgress.setValue(fileProg);
            currentFileProgress.setString("File hiện tại: " + fileProg + "%");
            totalProgress.setValue(totalProg);
            totalProgress.setString("Tổng thể: " + totalProg + "%");
        });
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 5));
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressLabel = new JLabel("Sẵn sàng...");
        currentFileProgress = new JProgressBar(0, 100);
        currentFileProgress.setStringPainted(true);
        currentFileProgress.setString("File hiện tại: 0%");
        totalProgress = new JProgressBar(0, 100);
        totalProgress.setStringPainted(true);
        totalProgress.setString("Tổng thể: 0%");
        
        // --- Mới: Thêm label cho tốc độ và ETR ---
        speedEtrLabel = new JLabel();
        speedEtrLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        progressPanel.add(progressLabel);
        progressPanel.add(currentFileProgress);
        progressPanel.add(totalProgress);
        progressPanel.add(speedEtrLabel); // Thêm vào panel

        statusLabel = new JLabel();
        statusLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        bottomPanel.add(progressPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.EAST);
        return bottomPanel;
    }

    // Các hàm còn lại giữ nguyên
    private void loadIcons() {
        int iconSize = 20;
        iconPlay = createIcon("/icons/play.png", iconSize, iconSize);
        iconStop = createIcon("/icons/stop.png", iconSize, iconSize);
        iconSend = createIcon("/icons/send.png", iconSize, iconSize);
        iconFolder = createIcon("/icons/folder.png", iconSize, iconSize);
        iconUsers = createIcon("/icons/users.png", iconSize, iconSize);
        iconLogout = createIcon("/icons/logout.png", iconSize, iconSize);
        int statusIconSize = 16;
        iconConnected = createIcon("/icons/connected.png", statusIconSize, statusIconSize);
        iconDisconnected = createIcon("/icons/disconnected.png", statusIconSize, statusIconSize);
    }
    private JPanel createTopControlPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(15, 10));
        JPanel connectionPanel = new JPanel();
        connectionPanel.setLayout(new BoxLayout(connectionPanel, BoxLayout.Y_AXIS));
        JPanel localPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        localPanel.setBorder(BorderFactory.createTitledBorder("Kết nối của bạn"));
        localPortField = new JTextField("9876", 5);
        listenButton = new JButton("Bắt đầu Lắng nghe", iconPlay);
        localPanel.add(new JLabel("Cổng lắng nghe:"));
        localPanel.add(localPortField);
        localPanel.add(listenButton);
        JPanel remotePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        remotePanel.setBorder(BorderFactory.createTitledBorder("Gửi File đến Peer"));
        remoteIpField = new JTextField("localhost", 15);
        remotePortField = new JTextField("9877", 5);
        remotePanel.add(new JLabel("IP Đích:"));
        remotePanel.add(remoteIpField);
        remotePanel.add(new JLabel("Cổng Đích:"));
        remotePanel.add(remotePortField);
        connectionPanel.add(localPanel);
        connectionPanel.add(remotePanel);
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        JPanel fileActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        browseButton = new JButton("Chọn Tệp/Thư mục...", iconFolder);
        sendButton = new JButton("Gửi", iconSend);
        fileActionPanel.add(browseButton);
        fileActionPanel.add(sendButton);
        JPanel systemActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if (currentUser.getRole() == User.Role.ADMIN) {
            JButton manageUsersButton = new JButton("Quản lý", iconUsers);
            styleButton(manageUsersButton, new Color(245, 124, 0));
            manageUsersButton.addActionListener(e -> new UserManagementDialog(this, authService).setVisible(true));
            systemActionPanel.add(manageUsersButton);
        }
        JButton logoutButton = new JButton("Đăng xuất", iconLogout);
        styleButton(logoutButton, new Color(211, 47, 47));
        logoutButton.addActionListener(e -> logout());
        systemActionPanel.add(logoutButton);
        rightPanel.add(fileActionPanel);
        rightPanel.add(systemActionPanel);
        topPanel.add(connectionPanel, BorderLayout.CENTER);
        topPanel.add(rightPanel, BorderLayout.EAST);
        JPanel savePanel = new JPanel(new BorderLayout(10, 0));
        savePanel.setBorder(BorderFactory.createTitledBorder("Thư mục lưu tệp nhận được"));
        savePathLabel = new JLabel(saveDirectory.getAbsolutePath());
        savePathLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        JButton changeDirButton = new JButton("Thay đổi...");
        changeDirButton.addActionListener(e -> chooseSaveDirectory());
        savePanel.add(new JLabel("Đường dẫn: "), BorderLayout.WEST);
        savePanel.add(savePathLabel, BorderLayout.CENTER);
        savePanel.add(changeDirButton, BorderLayout.EAST);
        JPanel fullTopPanel = new JPanel(new BorderLayout(10, 10));
        fullTopPanel.add(topPanel, BorderLayout.NORTH);
        fullTopPanel.add(savePanel, BorderLayout.CENTER);
        listenButton.addActionListener(e -> { if (isListening) stopListening(); else startListening(); });
        browseButton.addActionListener(e -> browseFiles());
        sendButton.addActionListener(e -> startSendingFiles());
        styleButton(browseButton, new Color(100, 100, 100));
        styleButton(sendButton, new Color(25, 118, 210));
        return fullTopPanel;
    }
    private void setupDragAndDrop() {
        setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) { return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor); }
            @Override
            public boolean importData(TransferSupport support) {
                try {
                    List<File> droppedFiles = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    prepareFilesToSend(droppedFiles);
                    return true;
                } catch (Exception e) { log("Lỗi khi kéo thả: " + e.getMessage()); }
                return false;
            }
        });
    }
    private void browseFiles() {
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            prepareFilesToSend(List.of(fileChooser.getSelectedFiles()));
        }
    }
    private void prepareFilesToSend(List<File> files) {
        filesToSend.clear();
        addFilesRecursively(filesToSend, files);
        if (!filesToSend.isEmpty()) {
            String fileList = filesToSend.size() > 1 ? filesToSend.size() + " tệp/thư mục" : filesToSend.get(0).getName();
            log("Đã chọn " + fileList + ". Sẵn sàng để gửi.");
            progressLabel.setText("Đã chọn: " + fileList);
        }
    }
    private void addFilesRecursively(List<File> allFiles, List<File> selectedFiles) {
        for (File file : selectedFiles) {
            if (file.isDirectory()) {
                addFilesRecursively(allFiles, List.of(Objects.requireNonNull(file.listFiles())));
            } else { allFiles.add(file); }
        }
    }
    private void sendPacket(String message, InetAddress address, int port) throws IOException {
        byte[] buffer = message.getBytes();
        socket.send(new DatagramPacket(buffer, buffer.length, address, port));
    }
    private ImageIcon createIcon(String path, int width, int height) {
        try {
            ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(path)));
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Không tìm thấy hoặc không thể resize icon: " + path);
            return null;
        }
    }
    private void chooseSaveDirectory() {
        JFileChooser dirChooser = new JFileChooser(saveDirectory);
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (dirChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            saveDirectory = dirChooser.getSelectedFile();
            savePathLabel.setText(saveDirectory.getAbsolutePath());
            log("Đã đổi thư mục lưu tệp thành: " + saveDirectory.getAbsolutePath());
        }
    }
    private void logout() {
        if (isListening) { stopListening(); }
        this.dispose();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
    private void updateStatus(boolean listening) {
        this.isListening = listening;
        if (listening) {
            statusLabel.setText("Đang lắng nghe...");
            statusLabel.setIcon(iconConnected);
            statusLabel.setForeground(new Color(0, 128, 0));
            listenButton.setText("Dừng");
            listenButton.setIcon(iconStop);
            styleButton(listenButton, new Color(198, 40, 40)); 
            localPortField.setEnabled(false);
        } else {
            statusLabel.setText("Không lắng nghe");
            statusLabel.setIcon(iconDisconnected);
            statusLabel.setForeground(Color.GRAY);
            listenButton.setText("Bắt đầu Lắng nghe");
            listenButton.setIcon(iconPlay);
            styleButton(listenButton, new Color(56, 142, 60)); 
            localPortField.setEnabled(true);
        }
    }
    public static String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) { return bytes + " B"; }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }
    private void refreshHistoryTable() {
        SwingUtilities.invokeLater(() -> {
            historyTableModel.setRowCount(0);
            var userHistory = historyService.getHistoryForUser(currentUser.getUsername());
            for (HistoryEntry entry : userHistory) {
                historyTableModel.addRow(new Object[]{
                        entry.getDirection(),
                        entry.getFileName(),
                        humanReadableByteCountSI(entry.getFileSize()),
                        entry.getRemotePeer(),
                        entry.getTimestamp().format(tableDateFormatter)
                });
            }
        });
    }
    private void loadInitialHistory() { refreshHistoryTable(); }
    private void stopListening() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        updateStatus(false);
        log("Đã dừng lắng nghe.");
    }
    private void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }
}