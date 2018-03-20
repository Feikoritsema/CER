import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;

public class MessageConsumer extends DefaultConsumer {

	private static Integer LOW_AVG_HEARTRATE = 40;

	private JsonMessageFactory messageFactory;

	private ArrayDeque<HeartrateMessage> heartrateMessages;

	MessageConsumer(final Channel channel) {
		super(channel);
		messageFactory = new JsonMessageFactory();
		heartrateMessages = new ArrayDeque<>(10);
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
			if (msg instanceof HeartrateMessage) {
				if (heartrateMessages.size() > 10)
					heartrateMessages.removeFirst();
				heartrateMessages.add((HeartrateMessage) msg);
			} /*else if (msg instanceof MovementMessage)
				worker.handleMessage(msg);
			else
				worker.handleMessage(msg);*/
		}
	}

	public Status updateStatus() {
		Integer sum = 0;
		Integer avg;
		if (!CollectionUtils.isEmpty(heartrateMessages)) {
			for (HeartrateMessage i : heartrateMessages)
				sum += i.getHeartrate();
			if ((avg = sum / heartrateMessages.size()) < LOW_AVG_HEARTRATE) {
				System.out.println("LOW AVERAGE HEARTRATE: " + avg.toString());
				return Status.UNHANDLED_EMERGENCY;
			}
		}
		return Status.OK;
	}
}
