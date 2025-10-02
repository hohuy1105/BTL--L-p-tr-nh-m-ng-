import javax.swing.*;
import java.awt.*;
import java.util.Objects; // <-- THÊM DÒNG NÀY
import com.formdev.flatlaf.FlatLightLaf;

public class LoginFrame extends JFrame {

    private final AuthService authService;
    private final HistoryService historyService;

    // --- Biến chứa các icon ---
    private ImageIcon iconLogin, iconRegister;

    public LoginFrame() {
        super("Đăng nhập / Đăng ký");
        authService = new AuthService();
        historyService = new HistoryService();

        loadIcons(); // <-- Tải icon

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
        JButton loginButton = new JButton("Đăng nhập", iconLogin); // <-- Gán icon
        JButton registerButton = new JButton("Đăng ký", iconRegister); // <-- Gán icon
        
        // --- Đặt màu cho các nút ---
        styleButton(loginButton, new Color(25, 118, 210)); // Xanh dương
        styleButton(registerButton, new Color(56, 142, 60)); // Xanh lá
        
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

    // --- Helper method để tải và resize icon ---
    private void loadIcons() {
        int iconSize = 18; // Kích thước icon cho nút đăng nhập/đăng ký
        iconLogin = createIcon("/icons/login.png", iconSize, iconSize);
        iconRegister = createIcon("/icons/register.png", iconSize, iconSize);
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

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Không thể cài đặt FlatLaf Look and Feel.");
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}