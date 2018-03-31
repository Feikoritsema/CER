package message;

public class EmergencyUpdateMessage extends Message {

    private String update;

    public EmergencyUpdateMessage() {
        super();
    }

    public EmergencyUpdateMessage(String update) {
        super();
        this.update = update;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
