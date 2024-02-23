package appMembers;

public class Organizer extends User{
    public Organizer(String username) {
        super(username, "organizer");
    }
    public boolean isOrganizer() {
        return true;
    }
    public boolean isAttendee() {
        return false;
    }
}
