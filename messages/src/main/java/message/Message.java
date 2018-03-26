package message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.time.LocalDateTime;


@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = HeartrateMessage.class, name = "heartratemessage"),
		@JsonSubTypes.Type(value = MovementMessage.class, name = "movementmessage"),
		@JsonSubTypes.Type(value = PanicMessage.class, name = "panicmessage"),
		@JsonSubTypes.Type(value = BedMessage.class, name = "bedmessage")
})

public class Message implements Serializable {

	private LocalDateTime time;

	public Message() {
		time = LocalDateTime.now();
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(final LocalDateTime time) {
		this.time = time;
	}
}
