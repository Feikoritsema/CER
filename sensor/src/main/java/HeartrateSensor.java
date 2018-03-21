import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.Random;

public class HeartrateSensor extends Sensor {

	private JLabel label;
	private Integer heartrate;

	public HeartrateSensor() {
		super("Heartrate Sensor");
	}

	@Override
	void createFrame() {
		heartrate = 70;
		setSize(300, 200);

		label = new JLabel("status");
		label.setText(heartrate.toString());
		label.setFont(new Font("Serif", Font.PLAIN, 50));
		label.setPreferredSize(new Dimension(300, 100));

		JTextField input = createNumberField();
		input.setPreferredSize(new Dimension(200, 50));
		input.addActionListener(e -> setHeartrate(Integer.valueOf(input.getText())));

		JButton submit = new JButton("Submit");
		submit.addActionListener(e -> setHeartrate(Integer.valueOf(input.getText())));

		LayoutManager layout = new BorderLayout();
		setLayout(layout);
		add(input, BorderLayout.LINE_START);
		add(submit, BorderLayout.LINE_END);
		add(label, BorderLayout.PAGE_END);

		pack();
		setVisible(true);
	}

	private void setHeartrate(final Integer rate) {
		heartrate = Optional.of(rate).filter(e -> e > 0).orElse(0);
		label.setText(heartrate.toString());
	}

	private JFormattedTextField createNumberField() {
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(200);
		return new JFormattedTextField(formatter);
	}

	void sendMessage() {
		Random rng = new Random();
		HeartrateMessage msg = new HeartrateMessage();
		int diff = rng.nextInt() & Integer.MAX_VALUE % 20;
		setHeartrate(heartrate - diff);//(heartrate < 70 ? heartrate + diff : heartrate - diff);
		msg.setHeartrate(heartrate);
		producer.sendDefaultMessage(msg);
	}

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

	public static void main(String argv[]) throws Exception {
		new Thread(new HeartrateSensor()).run();
	}
}
