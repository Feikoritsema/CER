package sensor;

import message.MovementMessage;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class MovementSensor extends Sensor {

	private Boolean isMoving;
	private JLabel label;
	private Integer msgCountLeft = 0;

	MovementSensor(final String host) {
		super("Movement Sensor", host);
	}

	@Override
	void createFrame() {
		setSize(300, 200);
		isMoving = false;
		label = new JLabel("status");
		label.setText(isMoving ? "Movement Detected" : "No Movememt");
		label.setFont(new Font("Serif", Font.PLAIN, 50));
		label.setPreferredSize(new Dimension(300, 100));

		JButton submit = new JButton("Move here");
		submit.addActionListener(e -> triggerMovement());

		LayoutManager layout = new BorderLayout();
		setLayout(layout);
		add(submit, BorderLayout.LINE_END);
		add(label, BorderLayout.PAGE_END);

		pack();
		setVisible(true);
	}

	private void triggerMovement() {
		isMoving = true;
		msgCountLeft = 3;
		label.setText("Movement Detected");
	}

	void sendMessage() {
		MovementMessage movementMessage = new MovementMessage();
		movementMessage.setMovement(true);
		movementMessage.setTime(LocalDateTime.now().minusHours(5));
		producer.sendDefaultMessage(movementMessage);
	}

	@Override
	public void run() {
		while (true) {
			while (isMoving && msgCountLeft > 0) {
				sendMessage();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				msgCountLeft--;
				if (msgCountLeft == 0)
					label.setText("Movement Absent");
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
