package message;

public class EmergencyMessage extends Message {

    public enum Action {
        OPEN,
        UPDATE,
        CLOSE
    }

    private final Action action;
    private final String body;

    public EmergencyMessage() {
        this(null);
    }

    public EmergencyMessage(Action action) {
        this(action, null);
    }

    public EmergencyMessage(Action action, String body) {
        super();
        this.action = action;
        this.body = body;
    }

    public Action getAction() {
        return action;
    }

    public String getBody() {
        return body;
    }

}
