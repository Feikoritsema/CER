import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class MessageProducer {
	public final static String CER_HUB_NORMAL = "CER_HUB_NORMAL";
	public final static String CER_HUB_PRIORITY = "CER_HUB_PRIORITY";

	private ConnectionFactory connectionFactory;

	private JsonMessageFactory messageFactory;

	public MessageProducer(final String host) {
		connectionFactory = new ConnectionFactory();
		messageFactory = new JsonMessageFactory();
		connectionFactory.setHost(host);
	}

	void sendDefaultMessage(final Message msg) {
		sendMessage(msg, CER_HUB_NORMAL);
	}

	void sendPriorityMessage(final Message msg) {
		sendMessage(msg, CER_HUB_PRIORITY);
	}

	private void sendMessage(final Message m, final String queue) {
		try {
			final Connection connection = connectionFactory.newConnection();
			final Channel channel = connection.createChannel();
			final String message = messageFactory.messageToJson(m);
			channel.basicPublish("", queue, null, message.getBytes());
			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
		System.out.println("[x] Sent");
	}
}
