import java.io.Serializable;

public class Message implements Serializable {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}
