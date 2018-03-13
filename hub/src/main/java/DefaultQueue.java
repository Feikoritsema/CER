import java.io.IOException;

public class DefaultQueue extends Queue {

	public final static String CER_HUB_NORMAL = "CER_HUB_NORMAL";

	private MessageConsumer consumer;

	DefaultQueue(final String host) {
		super(host, CER_HUB_NORMAL);
		consumer = new MessageConsumer(getChannel());
		try {
			getChannel().basicConsume(CER_HUB_NORMAL, true, consumer);
		} catch (IOException e) {
			System.out.println("IOException");
		}
	}


}
