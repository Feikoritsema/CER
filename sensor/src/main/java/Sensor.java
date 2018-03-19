public class Sensor {

	public static void main(String argv[]) throws Exception {
		MessageProducer producer = new MessageProducer("localhost");
		Integer i = 0;
		while (true) {
			Message msg = new Message();
			msg.setMessage("Test " + i.toString());
			producer.sendMessage(msg);
			Thread.sleep(2000);
		}
	}

}
