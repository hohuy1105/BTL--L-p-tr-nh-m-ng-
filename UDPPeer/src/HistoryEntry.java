import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryEntry {
    private final String username;
    private final String direction; // "Gửi" hoặc "Nhận"
    private final String fileName;
    private final String remotePeer; // IP:Port
    private final LocalDateTime timestamp;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public HistoryEntry(String username, String direction, String fileName, String remotePeer, LocalDateTime timestamp) {
        this.username = username;
        this.direction = direction;
        this.fileName = fileName;
        this.remotePeer = remotePeer;
        this.timestamp = timestamp;
    }

    // Getters
    public String getUsername() { return username; }
    public String getDirection() { return direction; }
    public String getFileName() { return fileName; }
    public String getRemotePeer() { return remotePeer; }
    public LocalDateTime getTimestamp() { return timestamp; }

    /**
     * Chuyển đối tượng thành chuỗi để lưu vào file.
     */
    public String toDataString() {
        return String.join("::", 
            username, 
            direction, 
            fileName, 
            remotePeer, 
            timestamp.format(FORMATTER));
    }

    /**
     * Tạo đối tượng từ chuỗi đọc trong file.
     */
    public static HistoryEntry fromDataString(String dataString) {
        try {
            String[] parts = dataString.split("::");
            if (parts.length == 5) {
                String username = parts[0];
                String direction = parts[1];
                String fileName = parts[2];
                String remotePeer = parts[3];
                LocalDateTime timestamp = LocalDateTime.parse(parts[4], FORMATTER);
                return new HistoryEntry(username, direction, fileName, remotePeer, timestamp);
            }
        } catch (Exception e) {
            System.err.println("Không thể phân tích dòng lịch sử: " + dataString);
            e.printStackTrace();
        }
        return null;
    }
}