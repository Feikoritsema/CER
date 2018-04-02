package hub;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import message.PanicMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PriorityQueue extends Queue {

    private final static String CER_HUB_PRIORITY = "CER_HUB_PRIORITY";

    @Autowired
    PriorityQueue(@Value("${host:localhost}") final String host, final Hub hub) {
        super(host, CER_HUB_PRIORITY, hub);
        AbstractMessageConsumer messageConsumer = new AbstractMessageConsumer(getChannel()) {
            @Override
            public void handleDelivery(final String consumerTag, final Envelope envelope, final AMQP.BasicProperties properties, final byte[] body) {
                super.handleDelivery(consumerTag, envelope, properties, body);
                List<PanicMessage> messageList = getMessagesOf(PanicMessage.class);
                update(messageList.get(messageList.size() - 1).getStatus());
            }
        };
        setConsumer(messageConsumer);
    }
}
