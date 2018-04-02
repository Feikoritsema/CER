package message;

import status.Status;

public class PanicMessage extends Message {

    private Status status;

    PanicMessage() {
        super();
    }

    public PanicMessage(final Status s) {
        super();
        status = s;
    }

    public Status getStatus() {
        return status;
    }
}
