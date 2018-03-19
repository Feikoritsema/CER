import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class HeartrateSensor {
	public final static String CER_HUB_NORMAL = "CER_HUB_NORMAL";

	public static void main(String argv[]) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(CER_HUB_NORMAL, false, false, false, null);

		HeartrateMessage msg = new HeartrateMessage();
		msg.setHeartrate(120);
		msg.setMessage("whoo");

		JsonMessageFactory messageFactory = new JsonMessageFactory();
		String message = messageFactory.messageToJson(msg);

		channel.basicPublish("", CER_HUB_NORMAL, null, message.getBytes());
		System.out.println(" [x] Sent");

		channel.close();
		connection.close();
		System.out.println("Ended connection");
	}

}
