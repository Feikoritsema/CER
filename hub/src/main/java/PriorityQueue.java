import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;

import java.util.List;

public class PriorityQueue extends Queue {

	public final static String CER_HUB_PRIORITY = "CER_HUB_PRIORITY";

	PriorityQueue(final String host) {
		super(host, CER_HUB_PRIORITY);
		AbstractMessageConsumer messageConsumer = new AbstractMessageConsumer(getChannel()) {
			@Override
			public void handleDelivery(final String consumerTag, final Envelope envelope, final AMQP.BasicProperties properties, final byte[] body) {
				super.handleDelivery(consumerTag, envelope, properties, body);
				List<PanicMessage> messageList = getMessagesOf(PanicMessage.class);
				notifyListeners(messageList.get(messageList.size() - 1).getStatus());
			}
		};
		setConsumer(messageConsumer);
	}
}
