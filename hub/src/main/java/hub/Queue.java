package hub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import status.Status;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

abstract class Queue {

    private final Hub hub;
    private final String queue;
    private Channel channel;
    private AbstractMessageConsumer consumer;

    Queue(final String host, final String queue, final Hub hub) {
        this.queue = queue;
        this.hub = hub;
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

    AbstractMessageConsumer getConsumer() {
        return consumer;
    }

    void setConsumer(final AbstractMessageConsumer consumer) {
        this.consumer = consumer;
        try {
            channel.basicConsume(queue, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Channel getChannel() {
        return channel;
    }

    void update(final Status s) {
        if (hub.getStatus() != s)
            hub.setStatus(s);
    }
}
