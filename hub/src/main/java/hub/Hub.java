package hub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import status.Status;

import javax.swing.*;
import java.awt.*;

@Component
public class Hub extends JFrame {

    private Status status = Status.OK;
    private final JLabel label;

    @Autowired
    public Hub(@Value("${host:localhost}") final String host) {
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

    public Status getStatus() {
        return status;
    }
}
