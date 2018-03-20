public class PanicMessage extends Message {

	private Status status;

	PanicMessage() {
		super();
	}

	PanicMessage(final Status s) {
		super();
		status = s;
	}

	public Status getStatus() {
		return status;
	}
}
