package sensor;

import message.MovementMessage;

import javax.swing.*;
import java.awt.*;

public class MovementSensor extends Sensor {

	private Boolean isMoving;
	private JLabel label;
	private Integer msgCountLeft = 0;

	private MovementSensor(final String host) {
		super("Movement Sensor", host);
	}

	@Override
	void createFrame() {
		setPreferredSize(new Dimension(600, 200));
		isMoving = true;
		label = new JLabel("status");
		label.setText(isMoving ? "Movement Detected" : "No Movememt");
		label.setFont(new Font("Serif", Font.PLAIN, 50));
		label.setPreferredSize(new Dimension(300, 100));

		final JButton submit = new JButton("Move here");
		submit.addActionListener(e -> triggerMovement());

		LayoutManager layout = new GridLayout(0, 1);
		setLayout(layout);
		add(submit);
		add(label);

		pack();
		setVisible(true);
	}

	private synchronized void triggerMovement() {
		isMoving = true;
		msgCountLeft = 3;
	}

	private void sendMessage() {
		MovementMessage movementMessage = new MovementMessage();
		movementMessage.setMovement(isMoving);
		producer.sendDefaultMessage(movementMessage);
		label.setText(isMoving ? "Movement Detected" : "No Movememt");
		if (msgCountLeft > 0)
			msgCountLeft--;
		else
			isMoving = false;
	}

	@Override
	public void run() {
		while (true) {
			sendMessage();
			try {
				Thread.sleep(5000);
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
		new Thread(new MovementSensor(host)).run();
	}
}
