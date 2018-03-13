import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class HeartrateSensor {
	public final static String CER_HUB_NORMAL = "CER_HUB_NORMAL";


	public static void main(String argv[]) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(CER_HUB_NORMAL, false, false, false, null);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);

		HeartrateMessage msg = new HeartrateMessage();

		msg.setHeartrate(120);
		msg.setMessage("whoo");
		oos.writeObject(msg);
		oos.flush();
		oos.close();

		String message = "Hello World!";
		channel.basicPublish("", CER_HUB_NORMAL, null, bos.toByteArray());
		System.out.println(" [x] Sent");
		bos.close();
		channel.close();
		connection.close();
		System.out.println("Ended connection");
	}

}
