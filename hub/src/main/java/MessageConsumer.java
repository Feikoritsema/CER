import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MessageConsumer extends DefaultConsumer {

	private Worker worker;

	public MessageConsumer(final Channel channel) {
		super(channel);
		worker = new Worker();
	}

	@Override
	public void handleDelivery(final String consumerTag, final Envelope envelope, final AMQP.BasicProperties properties, final byte[] body) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write(body, 0, body.length);
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());

		Message msg = null;
		try {
			ObjectInputStream objInputStream = new ObjectInputStream(is);
			msg = (Message) objInputStream.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("class not found");

		} catch (IOException e) {
			System.out.println("IOException...");
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
