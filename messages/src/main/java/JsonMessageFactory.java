import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class JsonMessageFactory {

	private ObjectMapper mapper;

	public JsonMessageFactory() {
		mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		mapper.registerModule(new JavaTimeModule()); // Timestamp support
	}

	public String messageToJson(final Message msg) throws JsonProcessingException {
		return mapper.writeValueAsString(msg);
	}

	public Message jsonToMessage(final String json) throws IOException {
		return mapper.readValue(json, Message.class);
	}

	public Message inputStreamToMessage(final ByteArrayInputStream is) throws IOException {
		return mapper.readValue(is, Message.class);
	}


}
