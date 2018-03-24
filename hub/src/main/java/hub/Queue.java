package hub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import status.Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

abstract class Queue extends Thread {

	private Channel channel;
	private AbstractMessageConsumer consumer;
	private List<QueueListener> listeners;
	private String queue;

	Queue(final String host, final String queue) {
		listeners = new ArrayList<>();
		this.queue = queue;
		final ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		try {
			final Connection connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(queue, false, true, false, null);
		} catch (TimeoutException | IOException e) {
			e.printStackTrace();
		}
	}

	public AbstractMessageConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(final AbstractMessageConsumer consumer) {
		this.consumer = consumer;
		try {
			channel.basicConsume(queue, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Channel getChannel() {
		return channel;
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
