import java.util.Random;

public class HeartrateSensor {

	public static void main(String argv[]) throws Exception {
		MessageProducer producer = new MessageProducer("localhost");
		Integer i = 0;
		Random rng = new Random();
		int heartrate = 70;
		while (true) {
			HeartrateMessage msg = new HeartrateMessage();
			msg.setMessage("Heartrate msg " + i.toString());
			int diff = rng.nextInt() % 20;
			heartrate = (heartrate < 70 ? heartrate + diff : heartrate - diff);
			msg.setHeartrate(heartrate);
			producer.sendMessage(msg);
			Thread.sleep(2000);
		}
	}

}
