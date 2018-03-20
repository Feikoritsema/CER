import javax.swing.*;
import java.awt.*;

public class HubApplication extends JFrame implements QueueListener {

	private Status status = Status.OK;
	private JLabel label;

	public HubApplication() {
		super("Hub");
		setSize(200, 200);

		// TODO: new priorityQueue
		System.out.println("I am the hub.");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(true);
		setLocationRelativeTo(null);
		label = new JLabel("status");
		label.setText(status.name());
		label.setPreferredSize(new Dimension(200, 200));
		getContentPane().add(label, BorderLayout.CENTER);

		final Queue defaultQueue = new DefaultQueue("localhost");
		defaultQueue.addQueueListener(this);
		defaultQueue.start();

		final Queue priorityQueue = new PriorityQueue("localhost");
		priorityQueue.addQueueListener(this);
		priorityQueue.start();

		pack();
		setVisible(true);
	}

	public static void main(String args[]) {
		new HubApplication();
	}

	public void setStatus(final Status s) {
		if (status == Status.UNHANDLED_EMERGENCY) {
			if (s == Status.HANDLED_EMERGENCY) {
				label.setForeground(Color.BLACK);
				status = s;
			}
		} else {
			status = s;
		}
		if (s == Status.UNHANDLED_EMERGENCY) {
			label.setForeground(Color.RED);
		}

		label.setText(status.name());
	}

	@Override
	public void onStatusChange(final Status s) {
		setStatus(s);
		System.out.println("Status: " + status.name());
	}
}
