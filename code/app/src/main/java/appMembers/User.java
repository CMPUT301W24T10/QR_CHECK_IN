package appMembers;

public abstract class User {
    private String username;
    private String role;
    private int UID;
    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }
    public String getUsername() {
        return username;
    }
    public String getRole() {
        return role;
    }
    public int getUID() {
        return UID;
    }
    public abstract boolean isOrganizer();
    public abstract boolean isAttendee();

}
