import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

abstract class Queue {

	private Channel channel;

	Queue(final String host, final String queue) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		try {
			Connection connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(queue, false, false, false, null);
		} catch (TimeoutException | IOException e) {
			e.printStackTrace();
		}
	}

	Channel getChannel() {
		return channel;
	}

}
