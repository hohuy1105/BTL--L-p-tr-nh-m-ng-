public enum PacketType {
    TOTAL_SIZE_HEADER("TOTAL_SIZE::"),
    FILE_HEADER("START_FILE::"),
    FILE_DATA("FILEDATA::"),
    FILE_EOF("END_FILE::"),
    TRANSMISSION_EOF("END_ALL::");

    private final String header;

    PacketType(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public static PacketType fromMessage(String message) {
        for (PacketType type : values()) {
            if (message.startsWith(type.getHeader())) {
                return type;
            }
        }
        return null;
    }
}