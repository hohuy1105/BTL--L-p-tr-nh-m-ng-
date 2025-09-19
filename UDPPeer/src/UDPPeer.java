import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Ứng dụng gửi & nhận tệp qua UDP với giao diện người dùng.
 */
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

    public UDPPeer() {
        super("UDP Peer File Transfer");
        initComponents();
        fileChooser = new JFileChooser();
    }

    private void initComponents() {
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Điều khiển"));

        JPanel localPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        localPortField = new JTextField("9876", 5);
        listenButton = new JButton("Bắt đầu Lắng nghe");
        localPanel.add(new JLabel("Lắng nghe trên cổng:"));
        localPanel.add(localPortField);
        localPanel.add(listenButton);

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

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Nhật ký hoạt động"));

        statusLabel = new JLabel("Trạng thái: Chưa lắng nghe.");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

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
            if (fileChooser.getSelectedFile() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một tệp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            new Thread(this::sendFile).start();
        });
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
                            }
                        } else {
                            if (fos != null) {
                                fos.write(packet.getData(), 0, packet.getLength());
                            }
                        }
                    }
                } catch (SocketException se) {
                    if (isListening) log("Lỗi socket: " + se.getMessage());
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

            // Gửi tên tệp
            String fileNameMessage = "FILENAME:" + file.getName();
            byte[] fileNameBytes = fileNameMessage.getBytes();
            socket.send(new DatagramPacket(fileNameBytes, fileNameBytes.length, remoteAddress, remotePort));

            // Gửi nội dung tệp
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    DatagramPacket dataPacket = new DatagramPacket(buffer, bytesRead, remoteAddress, remotePort);
                    socket.send(dataPacket);
                    Thread.sleep(1); // tránh tràn
                }
            }

            // Gửi tín hiệu EOF
            byte[] eof = "EOF".getBytes();
            socket.send(new DatagramPacket(eof, eof.length, remoteAddress, remotePort));
            log("Đã gửi tệp thành công.");

        } catch (Exception e) {
            log("Lỗi khi gửi tệp: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UDPPeer().setVisible(true));
    }
}
