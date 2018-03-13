import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sensor {

	public final static String CER_HUB_NORMAL = "CER_HUB_NORMAL";


	public static void main(String argv[]) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(CER_HUB_NORMAL, false, false, false, null);
		String message = "Hello World!";
		channel.basicPublish("", CER_HUB_NORMAL, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
		channel.close();
		connection.close();
		System.out.println("Ended connection");
	}

}
