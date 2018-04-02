package sensor;

import message.Message;
import message.PanicMessage;
import status.Status;

import javax.swing.*;
import java.awt.*;

public class PanicButton extends Sensor {

	private PanicButton(final String host) {
		super("Panic Button", host);
	}

	@Override
	void createFrame() {
		JButton submit = new JButton("PANIC!");
		submit.setPreferredSize(new Dimension(200, 200));
		submit.addActionListener(e -> sendMessage(new PanicMessage(Status.UNHANDLED_EMERGENCY)));

		JButton safebutton = new JButton("Nevermind...");
		safebutton.setPreferredSize(new Dimension(200, 200));
		safebutton.addActionListener(e -> sendMessage(new PanicMessage(Status.HANDLED_EMERGENCY)));

		LayoutManager layout = new BorderLayout();
		setLayout(layout);
		add(submit, BorderLayout.LINE_START);
		add(safebutton, BorderLayout.LINE_END);

		pack();
		setVisible(true);
	}

	private void sendMessage(final Message msg) {
		producer.sendPriorityMessage(msg);
	}

	public static void main(String args[]) throws Exception {
		String host = "localhost";
		if (args.length < 1){
			System.out.println("No host set, defaulting to localhost...");
		} else {
			host = args[0];
			System.out.println("Host set to: "+ host);
		}
		new PanicButton(host);
	}

	@Override
	public void run() {

	}
}
