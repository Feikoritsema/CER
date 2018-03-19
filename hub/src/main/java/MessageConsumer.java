import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MessageConsumer extends DefaultConsumer {

	private Worker worker;

	private JsonMessageFactory messageFactory;

	public MessageConsumer(final Channel channel) {
		super(channel);
		worker = new Worker();
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

		// TODO, fix instanceof..
		if (msg != null) {
			if (msg instanceof HeartrateMessage)
				worker.handleMessage((HeartrateMessage) msg);
			else if (msg instanceof MovementMessage)
				worker.handleMessage((MovementMessage) msg);
			else
				worker.handleMessage(msg);
		}
	}
}
