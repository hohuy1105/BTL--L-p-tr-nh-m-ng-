import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.CopyOnWriteArrayList;

public class HistoryService {
    private static final String HISTORY_FILE = "history.dat";
    // Sử dụng CopyOnWriteArrayList để an toàn khi truy cập từ nhiều luồng
    private final List<HistoryEntry> historyLog = new CopyOnWriteArrayList<>();

    public HistoryService() {
        loadHistory();
    }

    private void loadHistory() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String encryptedData = reader.readLine();
            if (encryptedData != null && !encryptedData.isEmpty()) {
                String decryptedData = CryptoUtils.decrypt(encryptedData);
                String[] lines = decryptedData.split("\n");
                for (String line : lines) {
                    if (line.isEmpty()) continue;
                    HistoryEntry entry = HistoryEntry.fromDataString(line);
                    if (entry != null) {
                        historyLog.add(entry);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải lịch sử: " + e.getMessage());
        }
    }

    private synchronized void saveHistory() {
        StringBuilder sb = new StringBuilder();
        for (HistoryEntry entry : historyLog) {
            sb.append(entry.toDataString()).append("\n");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
            if (sb.length() > 0) {
                String encryptedData = CryptoUtils.encrypt(sb.toString());
                writer.write(encryptedData);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu lịch sử: " + e.getMessage());
        }
    }

    public void addHistory(HistoryEntry entry) {
        historyLog.add(entry);
        saveHistory();
    }

    public List<HistoryEntry> getHistoryForUser(String username) {
        return historyLog.stream()
                .filter(entry -> entry.getUsername().equals(username))
                .collect(Collectors.toList());
    }
}