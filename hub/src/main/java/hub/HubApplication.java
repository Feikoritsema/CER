package hub;

import org.springframework.boot.SpringApplication;
import status.Status;

import javax.swing.*;
import java.awt.*;

public class HubApplication extends JFrame implements QueueListener {

	private Status status = Status.OK;
	private JLabel lockstatus;
	private final JLabel label;

	public static HubApplication INSTANCE;
	private static String host = "localhost";

	private HubApplication(final String host) {
		super("Hub @" + host);
		setPreferredSize(new Dimension(600, 200));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(true);
		setLocationRelativeTo(null);

		label = new JLabel("status");
		label.setText(status.name());
		label.setFont(new Font("Serif", Font.PLAIN, 50));
		label.setPreferredSize(new Dimension(200, 200));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(label, BorderLayout.CENTER);

		lockstatus = new JLabel("Lock is closed");
		getContentPane().add(lockstatus, BorderLayout.CENTER);

		final Queue defaultQueue = new DefaultQueue(host);
		defaultQueue.addQueueListener(this);
		defaultQueue.start();

		final Queue priorityQueue = new PriorityQueue(host);
		priorityQueue.addQueueListener(this);
		priorityQueue.start();

		pack();
		setVisible(true);
	}

	public synchronized void setStatus(final Status s) {
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

	public static void main(final String args[]) {
		if (args.length < 1){
			System.out.println("No host set, defaulting to localhost...");
		} else {
			host = args[0];
			System.out.println("Host set to: "+ host);
		}
		INSTANCE = new HubApplication(host);
		SpringApplication.run(RestApplication.class, args);
	}

	public static HubApplication getInstance() {
		if (INSTANCE == null)
			INSTANCE = new HubApplication(host);
		return INSTANCE;
	}

	public void openLock(){
		lockstatus.setText("Lock is open");
	}
}
