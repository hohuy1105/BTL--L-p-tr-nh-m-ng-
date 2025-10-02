import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryEntry {
    private final String username;
    private final String direction; 
    private final String fileName;
    private final String remotePeer; 
    private final LocalDateTime timestamp;
    private final long fileSize;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public HistoryEntry(String username, String direction, String fileName, String remotePeer, LocalDateTime timestamp, long fileSize) {
        this.username = username;
        this.direction = direction;
        this.fileName = fileName;
        this.remotePeer = remotePeer;
        this.timestamp = timestamp;
        this.fileSize = fileSize;
    }

    // Getters
    public String getUsername() { return username; }
    public String getDirection() { return direction; }
    public String getFileName() { return fileName; }
    public String getRemotePeer() { return remotePeer; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public long getFileSize() { return fileSize; }

    public String toDataString() {
        // Cập nhật để thêm fileSize
        return String.join("::", 
            username, 
            direction, 
            fileName, 
            remotePeer, 
            timestamp.format(FORMATTER),
            String.valueOf(fileSize));
    }

    public static HistoryEntry fromDataString(String dataString) {
        try {
            String[] parts = dataString.split("::");
            if (parts.length == 6) {
                String username = parts[0];
                String direction = parts[1];
                String fileName = parts[2];
                String remotePeer = parts[3];
                LocalDateTime timestamp = LocalDateTime.parse(parts[4], FORMATTER);
                long fileSize = Long.parseLong(parts[5]);
                return new HistoryEntry(username, direction, fileName, remotePeer, timestamp, fileSize);
            }
        } catch (Exception e) {
            System.err.println("Không thể phân tích dòng lịch sử: " + dataString);
            e.printStackTrace();
        }
        return null;
    }
}