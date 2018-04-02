package hub;

import hub.settings.Neighbour;
import hub.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import status.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

@Component
public class Hub extends JFrame {

    private Status status = Status.OK;
    private final JLabel label;
    private Settings settings;
    private String address;

    @Autowired
    public Hub(@Value("${host:localhost}") final String host, final Settings settings) {
        super("Hub @" + host);
        this.settings = settings;
        this.address = host;
        setPreferredSize(new Dimension(800, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);
        setLocationRelativeTo(null);

        label = new JLabel("status");
        label.setText(status.name());
        label.setFont(new Font("Serif", Font.PLAIN, 50));
        label.setPreferredSize(new Dimension(200, 200));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);

        final JPanel addresses = new JPanel(new GridLayout(1, 2));

        final JList<Neighbour> neighbourJList = new JList<>(settings.getNeighbours());
        JScrollPane pane = new JScrollPane(neighbourJList);
        neighbourJList.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    int idx = neighbourJList.getSelectedIndex();
                    if (idx != -1) {
                        settings.getNeighbours().remove(idx);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        addresses.add(pane);

        final JLabel emergencyServices = new JLabel(settings.getEmergencyService());
        addresses.add(emergencyServices);

        add(addresses, BorderLayout.SOUTH);


        final JComboBox<String> options = new JComboBox<>(new String[]{"Neighbour", "Emergency Service"});

        final JTextField input = new JFormattedTextField();
        input.setPreferredSize(new Dimension(600, 50));
        input.addActionListener((e) -> {
            final String type = options.getSelectedIndex() == 1 ? "emergency_service" : "neighbour";
            if (addAddressToSettings(input.getText(), type)) {
                if (Objects.equals(type, "emergency_service")) {
                    emergencyServices.setText(input.getText());
                }
                input.setText("");
            }
        });

        final JPanel addressPanel = new JPanel(new GridLayout(1, 3));
        addressPanel.add(input);
        addressPanel.add(options);

        add(addressPanel, BorderLayout.CENTER);

        setResizable(false);
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

    private boolean addAddressToSettings(final String message, final String type) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(message);
        try {
            restTemplate.exchange("http://" + address + ":8080/api/settings/" + type,
                    HttpMethod.POST, request, String.class);
        } catch (HttpStatusCodeException e) {
            System.err.println("Address is invalid.. ");
            return false;
        } catch (Exception e) {
            System.err.println("Internal Server Error.. ");
            return false;
        }
        return true;
    }

    public Boolean validateNeighbour(String address){
        return true;
    }
}
