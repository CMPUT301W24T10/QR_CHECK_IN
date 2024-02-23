package appMembers;

public class Attendee extends User{
    public Attendee(String username) {
        super(username, "attendee");
    }
    public boolean isOrganizer() {
        return false;
    }
    public boolean isAttendee() {
        return true;
    }
}
