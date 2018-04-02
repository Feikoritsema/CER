package sensor;

import javax.swing.*;

abstract class Sensor extends JFrame implements Runnable {

	final MessageProducer producer;

	Sensor(final String title, final String host) {
		super(title + " @" + host);
		producer = new MessageProducer(host);
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		createFrame();
	}

	abstract void createFrame();
}
