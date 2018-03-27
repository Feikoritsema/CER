package sensor;

import message.BedMessage;
import status.BedSensorStatus;

import javax.swing.*;
import java.awt.*;

public class BedSensor extends Sensor {

	private BedSensorStatus status;
	private JLabel label;

	BedSensor(final String host) {
		super("Bed Sensor", host);
	}

	@Override
	void createFrame() {
		setSize(300, 200);
		status = BedSensorStatus.NOT_ASLEEP;
		label = new JLabel("status");
		label.setText(status.name());
		label.setFont(new Font("Serif", Font.PLAIN, 50));
		label.setPreferredSize(new Dimension(400, 100));
		label.setHorizontalAlignment(SwingConstants.CENTER);

		JButton sleep = new JButton("Go to sleep");
		JButton wake = new JButton("Wake up");
		JButton panic = new JButton("Emergency Situation");
		sleep.addActionListener(e -> setStatus(BedSensorStatus.ASLEEP));
		wake.addActionListener(e -> setStatus(BedSensorStatus.NOT_ASLEEP));
		panic.addActionListener(e -> setStatus(BedSensorStatus.ALARM));

		LayoutManager layout = new GridLayout(0, 3);
		setLayout(layout);
		add(sleep);
		add(wake);
		add(panic);
		add(label, BorderLayout.PAGE_END);

		pack();
		setVisible(true);
	}

	void sendMessage() {
		BedMessage message = new BedMessage();
		message.setStatus(status);
		producer.sendDefaultMessage(message);
	}

	@Override
	public void run() {
		while (true) {
			sendMessage();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) throws Exception {
		String host = "localhost";
		if (args.length < 1){
			System.out.println("No host set, defaulting to localhost...");
		} else {
			host = args[0];
			System.out.println("Host set to: "+ host);
		}
		new Thread(new BedSensor(host)).run();
	}

	public void setStatus(final BedSensorStatus status) {
		this.status = status;
		label.setText(status.name());
	}
}
