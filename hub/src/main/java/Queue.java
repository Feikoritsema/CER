import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

abstract class Queue extends Thread {

	private MessageConsumer consumer;
	private List<QueueListener> listeners;

	Queue(final String host, final String queue) {
		listeners = new ArrayList<>();
		final ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		try {
			final Connection connection = factory.newConnection();
			final Channel channel = connection.createChannel();
			consumer = new MessageConsumer(channel);
			channel.queueDeclare(queue, false, true, false, null);
			channel.basicConsume(queue, true, consumer);
		} catch (TimeoutException | IOException e) {
			e.printStackTrace();
		}
	}

	public MessageConsumer getConsumer() {
		return consumer;
	}

	public void addQueueListener(QueueListener queueListener) {
		listeners.add(queueListener);
	}

	public void removeQueueListener(QueueListener queueListener) {
		listeners.remove(queueListener);
	}

	public void notifyListeners(final Status s) {
		listeners.forEach(e -> e.onStatusChange(s));
	}
}
