import javax.swing.*;
import java.awt.*;

public class HubApplication extends JFrame implements QueueListener {

	private Status status = Status.OK;
	private JLabel label;

	public HubApplication() {
		super("Hub");
		setSize(200, 200);
		final Queue defaultQueue = new DefaultQueue("localhost");
		defaultQueue.addQueueListener(this);
		defaultQueue.start();

		// TODO: new priorityQueue
		System.out.println("I am the hub.");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(true);
		setLocationRelativeTo(null);
		label = new JLabel("status");
		label.setText(status.name());
		label.setPreferredSize(new Dimension(200, 200));
		getContentPane().add(label, BorderLayout.CENTER);

		pack();
		setVisible(true);
	}

	public static void main(String args[]) {
		new HubApplication();
	}

	public void setStatus(final Status s) {
		status = s;
		if (s == Status.UNHANDLED_EMERGENCY) {
			label.setForeground(Color.RED);
		}
		label.setText(status.name());
	}

	@Override
	public void onStatusChange(final Status s) {
		System.out.println("Default Queue status: " + s.name());
		setStatus(s);
	}
}
