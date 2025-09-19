import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final AuthService authService;
    private final HistoryService historyService;

    public LoginFrame() {
        super("Đăng nhập / Đăng ký");
        authService = new AuthService();
        historyService = new HistoryService();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.add(new JLabel("Tên đăng nhập:"));
        JTextField userField = new JTextField(15);
        userPanel.add(userField);

        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        passPanel.add(new JLabel("Mật khẩu:"));
        JPasswordField passField = new JPasswordField(15);
        passPanel.add(passField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Đăng nhập");
        JButton registerButton = new JButton("Đăng ký");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        add(userPanel);
        add(passPanel);
        add(buttonPanel);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            User user = authService.authenticate(username, password);

            if (user != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Chào mừng " + user.getUsername());
                dispose(); // Đóng cửa sổ đăng nhập
                // Mở ứng dụng chính
                new UDPPeer(user, authService, historyService).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Nhập tên đăng nhập mới:");
            if (username == null || username.trim().isEmpty()) {
                return;
            }

            String password = JOptionPane.showInputDialog(this, "Nhập mật khẩu:");
            if (password == null || password.isEmpty()) {
                return;
            }

            if (authService.registerUser(username.trim(), password, User.Role.USER)) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.");
            } else {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}