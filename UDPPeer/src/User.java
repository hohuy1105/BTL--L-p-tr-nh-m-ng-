public class User {
    public enum Role { USER, ADMIN }

    private String username;
    private String hashedPassword;
    private Role role;

    public User(String username, String hashedPassword, Role role) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    // Getters
    public String getUsername() { return username; }
    public String getHashedPassword() { return hashedPassword; }
    public Role getRole() { return role; }

    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
}