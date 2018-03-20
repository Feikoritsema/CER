import javax.swing.*;

abstract class Sensor extends JFrame implements Runnable {

	MessageProducer producer;

	Sensor(final String title) {
		super(title);
		producer = new MessageProducer("localhost");
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		createFrame();
	}

	abstract void createFrame();

	abstract void sendMessage();

	@Override
	public void run() {
		while (true) {
			sendMessage();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
