import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthService {
    private static final String USERS_FILE = "users.dat";
    private final Map<String, User> userMap = new ConcurrentHashMap<>();

    public AuthService() {
        loadUsers();
    }

    private void loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            // Tạo tài khoản admin mặc định nếu file không tồn tại
            registerUser("admin", "admin", User.Role.ADMIN);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String encryptedLine = reader.readLine();
            if (encryptedLine != null && !encryptedLine.isEmpty()) {
                String decryptedData = CryptoUtils.decrypt(encryptedLine);
                String[] lines = decryptedData.split("\n");
                for (String line : lines) {
                    if (line.isEmpty()) continue;
                    String[] parts = line.split("::");
                    if (parts.length == 3) {
                        String username = parts[0];
                        String hashedPassword = parts[1];
                        User.Role role = User.Role.valueOf(parts[2]);
                        userMap.put(username, new User(username, hashedPassword, role));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải dữ liệu người dùng: " + e.getMessage());
            // Có thể tạo file backup hoặc thông báo lỗi nghiêm trọng
        }
    }

    private void saveUsers() {
        StringBuilder sb = new StringBuilder();
        for (User user : userMap.values()) {
            sb.append(user.getUsername())
              .append("::")
              .append(user.getHashedPassword())
              .append("::")
              .append(user.getRole().name())
              .append("\n");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            if (sb.length() > 0) {
                String encryptedData = CryptoUtils.encrypt(sb.toString());
                writer.write(encryptedData);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu dữ liệu người dùng: " + e.getMessage());
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean registerUser(String username, String password, User.Role role) {
        if (userMap.containsKey(username)) {
            return false; // Tên người dùng đã tồn tại
        }
        String hashedPassword = hashPassword(password);
        User newUser = new User(username, hashedPassword, role);
        userMap.put(username, newUser);
        saveUsers();
        return true;
    }

    public User authenticate(String username, String password) {
        User user = userMap.get(username);
        if (user != null && user.getHashedPassword().equals(hashPassword(password))) {
            return user;
        }
        return null; // Xác thực thất bại
    }

    // Các phương thức cho Admin
    public Collection<User> getAllUsers() {
        return userMap.values();
    }

    public void deleteUser(String username) {
        // Không cho phép xóa admin
        if (!"admin".equalsIgnoreCase(username) && userMap.containsKey(username)) {
            userMap.remove(username);
            saveUsers();
        }
    }
}