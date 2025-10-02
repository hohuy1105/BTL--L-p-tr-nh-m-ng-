import javax.swing.*;
import java.awt.*;

public class UserManagementDialog extends JDialog {
    private final AuthService authService;
    private final DefaultListModel<User> listModel;
    private final JList<User> userList;

    public UserManagementDialog(Frame owner, AuthService authService) {
        super(owner, "Quản lý người dùng", true);
        this.authService = authService;
        setSize(400, 300);
        setLocationRelativeTo(owner);

        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel);
        refreshUserList();

        JButton addButton = new JButton("Thêm");
        addButton.addActionListener(e -> addUser());

        JButton deleteButton = new JButton("Xóa");
        deleteButton.addActionListener(e -> deleteUser());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        add(new JScrollPane(userList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void refreshUserList() {
        listModel.clear();
        authService.getAllUsers().forEach(listModel::addElement);
    }

    private void addUser() {
        String username = JOptionPane.showInputDialog(this, "Nhập tên người dùng mới:");
        if (username == null || username.trim().isEmpty()) return;

        String password = JOptionPane.showInputDialog(this, "Nhập mật khẩu cho " + username + ":");
        if (password == null || password.isEmpty()) return;

        if (authService.registerUser(username.trim(), password, User.Role.USER)) {
            JOptionPane.showMessageDialog(this, "Thêm người dùng thành công!");
            refreshUserList();
        } else {
            JOptionPane.showMessageDialog(this, "Tên người dùng đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        User selectedUser = userList.getSelectedValue();
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một người dùng để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("admin".equalsIgnoreCase(selectedUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản admin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa " + selectedUser.getUsername() + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            authService.deleteUser(selectedUser.getUsername());
            refreshUserList();
        }
    }
}