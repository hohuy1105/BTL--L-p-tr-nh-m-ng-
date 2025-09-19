import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UDPPeer extends JFrame {

    private JTextArea logArea;
    private JTextField remoteIpField;
    private JTextField remotePortField;
    private JTextField localPortField;
    private JFileChooser fileChooser;
    private JLabel statusLabel;
    private JButton listenButton;

    private DatagramSocket socket;
    private boolean isListening = false;

    private final User currentUser;
    private final AuthService authService;
    private final HistoryService historyService;

    // --- Các thành phần giao diện mới cho lịch sử ---
    private JTable historyTable;
    private DefaultTableModel historyTableModel;
    private final DateTimeFormatter tableDateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");

    public UDPPeer(User user, AuthService authService, HistoryService historyService) {
        super("UDP Peer File Transfer - Logged in as: " + user.getUsername());
        this.currentUser = user;
        this.authService = authService;
        this.historyService = historyService;

        initComponents();
        fileChooser = new JFileChooser();
        loadInitialHistory(); // Tải lịch sử ban đầu cho người dùng
    }

    private void initComponents() {
        setSize(700, 600); // Tăng kích thước cửa sổ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Điều khiển"));

        // ... (Các panel điều khiển giữ nguyên như cũ)
        JPanel localPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        localPortField = new JTextField("9876", 5);
        listenButton = new JButton("Bắt đầu Lắng nghe");
        localPanel.add(new JLabel("Lắng nghe trên cổng:"));
        localPanel.add(localPortField);
        localPanel.add(listenButton);

        if (currentUser.getRole() == User.Role.ADMIN) {
            JButton manageUsersButton = new JButton("Quản lý người dùng");
            manageUsersButton.addActionListener(e -> {
                UserManagementDialog dialog = new UserManagementDialog(this, authService);
                dialog.setVisible(true);
            });
            localPanel.add(manageUsersButton);
        }

        JPanel remotePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        remoteIpField = new JTextField("localhost", 15);
        remotePortField = new JTextField("9877", 5);
        remotePanel.add(new JLabel("IP Peer Đích:"));
        remotePanel.add(remoteIpField);
        remotePanel.add(new JLabel("Cổng Đích:"));
        remotePanel.add(remotePortField);

        JPanel sendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton browseButton = new JButton("Chọn Tệp...");
        JButton sendButton = new JButton("Gửi Tệp");
        sendPanel.add(browseButton);
        sendPanel.add(sendButton);

        controlPanel.add(localPanel);
        controlPanel.add(remotePanel);
        controlPanel.add(sendPanel);

        // --- Khu vực trung tâm được thiết kế lại ---
        // Bảng lịch sử
        String[] columnNames = {"Hành động", "Tên tệp", "Đối tác (Peer)", "Thời gian"};
        historyTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa bảng
            }
        };
        historyTable = new JTable(historyTableModel);
        JScrollPane historyScrollPane = new JScrollPane(historyTable);
        historyScrollPane.setBorder(BorderFactory.createTitledBorder("Lịch sử gửi nhận"));

        // Nhật ký hoạt động
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Nhật ký hoạt động"));

        // JSplitPane để chia đôi khu vực
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, historyScrollPane, logScrollPane);
        splitPane.setResizeWeight(0.6); // Lịch sử chiếm 60% không gian ban đầu

        statusLabel = new JLabel("Trạng thái: Chưa lắng nghe.");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(controlPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER); // Thêm splitPane vào trung tâm
        add(statusLabel, BorderLayout.SOUTH);

        // ... (Các action listener giữ nguyên như cũ)
        listenButton.addActionListener(e -> {
            if (isListening) stopListening();
            else startListening();
        });

        browseButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                log("Đã chọn tệp: " + fileChooser.getSelectedFile().getName());
            }
        });

        sendButton.addActionListener(e -> {
            if (!isListening) {
                JOptionPane.showMessageDialog(this, "Vui lòng 'Bắt đầu Lắng nghe' trước khi gửi file!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (fileChooser.getSelectedFile() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một tệp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            new Thread(this::sendFile).start();
        });
    }

    private void loadInitialHistory() {
        refreshHistoryTable();
    }
    
    private void refreshHistoryTable() {
        // Xóa dữ liệu cũ
        historyTableModel.setRowCount(0);
        // Lấy lịch sử cho người dùng hiện tại
        var userHistory = historyService.getHistoryForUser(currentUser.getUsername());
        // Thêm từng bản ghi vào bảng
        for (HistoryEntry entry : userHistory) {
            historyTableModel.addRow(new Object[]{
                    entry.getDirection(),
                    entry.getFileName(),
                    entry.getRemotePeer(),
                    entry.getTimestamp().format(tableDateFormatter)
            });
        }
    }

    private void startListening() {
        try {
            int port = Integer.parseInt(localPortField.getText());
            socket = new DatagramSocket(port);
            isListening = true;
            statusLabel.setText("Trạng thái: Đang lắng nghe trên cổng " + port);
            listenButton.setText("Kết thúc Lắng nghe");
            localPortField.setEnabled(false);
            log("Bắt đầu lắng nghe trên cổng " + port);

            Thread listener = new Thread(() -> {
                FileOutputStream fos = null;
                File receivingFile = null;
                try {
                    byte[] buffer = new byte[65507];
                    while (isListening) {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);

                        String message = new String(packet.getData(), 0, packet.getLength());

                        if (message.startsWith("FILENAME:")) {
                            if (fos != null) fos.close();
                            String fileName = message.substring(9);
                            log("Đang nhận tệp: " + fileName + " từ " + packet.getAddress().getHostAddress());
                            receivingFile = new File("received_" + fileName);
                            fos = new FileOutputStream(receivingFile);
                        } else if (message.equals("EOF")) {
                            if (fos != null) {
                                fos.close();
                                fos = null;
                                log("Đã nhận xong tệp: " + receivingFile.getName());

                                // --- THÊM LỊCH SỬ KHI NHẬN FILE ---
                                String remotePeer = packet.getAddress().getHostAddress() + ":" + packet.getPort();
                                HistoryEntry entry = new HistoryEntry(
                                    currentUser.getUsername(), "Nhận", receivingFile.getName(), remotePeer, LocalDateTime.now()
                                );
                                historyService.addHistory(entry);
                                SwingUtilities.invokeLater(this::refreshHistoryTable);
                            }
                        } else {
                            if (fos != null) {
                                fos.write(packet.getData(), 0, packet.getLength());
                            }
                        }
                    }
                } catch (SocketException se) {
                    if (isListening) log("Socket đã đóng.");
                } catch (Exception e) {
                    log("Lỗi khi nhận dữ liệu: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) fos.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            listener.setDaemon(true);
            listener.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể lắng nghe: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stopListening() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        isListening = false;
        statusLabel.setText("Trạng thái: Chưa lắng nghe.");
        listenButton.setText("Bắt đầu Lắng nghe");
        localPortField.setEnabled(true);
        log("Đã dừng lắng nghe.");
    }

    private void sendFile() {
        try {
            InetAddress remoteAddress = InetAddress.getByName(remoteIpField.getText());
            int remotePort = Integer.parseInt(remotePortField.getText());
            File file = fileChooser.getSelectedFile();

            log("Đang gửi tệp: " + file.getName() + " tới " + remoteAddress.getHostAddress() + ":" + remotePort);

            String fileNameMessage = "FILENAME:" + file.getName();
            byte[] fileNameBytes = fileNameMessage.getBytes();
            socket.send(new DatagramPacket(fileNameBytes, fileNameBytes.length, remoteAddress, remotePort));

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    DatagramPacket dataPacket = new DatagramPacket(buffer, bytesRead, remoteAddress, remotePort);
                    socket.send(dataPacket);
                    Thread.sleep(1); 
                }
            }

            byte[] eof = "EOF".getBytes();
            socket.send(new DatagramPacket(eof, eof.length, remoteAddress, remotePort));
            log("Đã gửi tệp thành công.");
            
            // --- THÊM LỊCH SỬ KHI GỬI FILE ---
            String remotePeer = remoteAddress.getHostAddress() + ":" + remotePort;
            HistoryEntry entry = new HistoryEntry(
                currentUser.getUsername(), "Gửi", file.getName(), remotePeer, LocalDateTime.now()
            );
            historyService.addHistory(entry);
            SwingUtilities.invokeLater(this::refreshHistoryTable);

        } catch (Exception e) {
            log("Lỗi khi gửi tệp: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }
}