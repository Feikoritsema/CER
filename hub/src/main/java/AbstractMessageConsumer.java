import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractMessageConsumer extends DefaultConsumer {
	public static final int MAX_ELEMENTS = 10;

	private final JsonMessageFactory messageFactory;
	private List<Message> messages;

	public AbstractMessageConsumer(final Channel channel) {
		super(channel);
		messages = new ArrayList<>();
		messageFactory = new JsonMessageFactory();
	}

	@Override
	public void handleDelivery(final String consumerTag, final Envelope envelope, final AMQP.BasicProperties properties, final byte[] body) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write(body, 0, body.length);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		Message msg;
		try {
			msg = messageFactory.inputStreamToMessage(is);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		if (msg != null) {
			messages.add(msg);
		}
	}

	<T extends Message> List<T> getMessagesOf(Class<T> clazz) {
		return messages
				.stream()
				.filter(clazz::isInstance)
				.map(clazz::cast)
				.collect(Collectors.toList());
	}

	<T extends Message> void clearMessages(List<T> toRemove) {
		messages.removeAll(toRemove);
	}
}
