import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;


@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = HeartrateMessage.class, name = "heartratemessage"),
		@JsonSubTypes.Type(value = MovementMessage.class, name = "movementmessage"),
		@JsonSubTypes.Type(value = PanicMessage.class, name = "panicmessage")
})
public class Message implements Serializable {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}
