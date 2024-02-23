package appMembers;
/**
 * @author sarao
 * User class is an abstract class that is used to create the Organizer and Attendee classes.
 * It contains the username, role, and UID of the user.
 * It also contains abstract methods that are used to check if the user is an organizer or an attendee.
 */

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
