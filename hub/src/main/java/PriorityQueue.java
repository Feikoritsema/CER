import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PriorityQueue extends Queue {

	public final static String CER_HUB_PRIORITY = "CER_HUB_PRIORITY";

	PriorityQueue(final String host) {
		super(host, CER_HUB_PRIORITY);
		JsonMessageFactory messageFactory = new JsonMessageFactory();
		MessageConsumer messageConsumer = new MessageConsumer(getChannel()) {
			@Override
			public void handleDelivery(final String consumerTag, final Envelope envelope, final AMQP.BasicProperties properties, final byte[] body) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				os.write(body, 0, body.length);
				ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
				PanicMessage msg;
				try {
					msg = (PanicMessage) messageFactory.inputStreamToMessage(is);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				notifyListeners(msg.getStatus());
			}
		};
		setConsumer(messageConsumer);
	}
}
