package hub;

import hub.settings.Neighbour;
import hub.settings.Settings;
import message.EmergencyMessage;
import message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import status.Status;
import tcp.ClientHandler;
import tcp.LastingClientHandler;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class Hub extends JFrame {

    private Status status = Status.OK;
    private JLabel label;
    private final Settings settings;
    private final String address;

    private BlockingQueue<Message> queue;


    @Autowired
    public Hub(@Value("${host:localhost}") final String host, final Settings settings) {
        super("Hub @" + host);
        this.settings = settings;
        this.address = host;
        setPreferredSize(new Dimension(800, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);
        setLocationRelativeTo(null);

        createStatusLabel();
        createAddressPanel();

        setResizable(false);
        pack();
        setVisible(true);
    }

    private void createStatusLabel() {
        label = new JLabel("status");
        label.setText(status.name());
        label.setFont(new Font("Serif", Font.PLAIN, 50));
        label.setPreferredSize(new Dimension(200, 200));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);
    }

    private void createAddressPanel() {
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

        JPanel secondaryAddresses = new JPanel();
        GridLayout saLayout = new GridLayout(2,1);
        secondaryAddresses.setLayout(saLayout);

        Border saBorder = secondaryAddresses.getBorder();
        Border saMargin = new EmptyBorder(10, 10, 10, 10);
        secondaryAddresses.setBorder(new CompoundBorder(saBorder, saMargin));

        final JLabel emergencyServices = new JLabel("Emergency services: " + settings.getEmergencyService());
        secondaryAddresses.add(emergencyServices);

        final JLabel smartLock = new JLabel("Smart lock: " + settings.getLock());
        secondaryAddresses.add(smartLock);

        addresses.add(secondaryAddresses, BorderLayout.LINE_END);
        add(addresses, BorderLayout.SOUTH);

        final JComboBox<String> options = new JComboBox<>(new String[]{"Neighbour", "Emergency Service", "Smart Lock"});

        final JTextField input = new JFormattedTextField();
        input.setPreferredSize(new Dimension(600, 50));
        input.addActionListener((e) -> {
            final String type;
            switch (options.getSelectedIndex()) {
                case 0:
                default:
                    type = "neighbour";
                    break;
                case 1:
                    type = "emergency_service";
                    break;
                case 2:
                    type = "smart_lock";
                    break;
            }

            if (addAddressToSettings(input.getText(), type)) {
                if (Objects.equals(type, "emergency_service")) {
                    emergencyServices.setText("Emergency services: " + input.getText());
                }
                if (Objects.equals(type, "smart_lock")) {
                    smartLock.setText("Smart lock: " + input.getText());
                }
                input.setText("");
            }
        });

        final JPanel addressPanel = new JPanel(new GridLayout(1, 3));
        addressPanel.add(input);
        addressPanel.add(options);

        add(addressPanel, BorderLayout.CENTER);
    }

    public synchronized void setStatus(final Status s) {
        // TODO: Implement sending message when neighbour reacts
        if (status == Status.OK && s == Status.UNHANDLED_EMERGENCY) {
            initializeEmergencySequence();
        }

        if (status == Status.UNHANDLED_EMERGENCY) {
            if (s == Status.HANDLED_EMERGENCY) {
                label.setForeground(Color.BLACK);
                status = s;
                sendMessageToQueue(new EmergencyMessage(EmergencyMessage.Action.CLOSE, "Emergency resolved."));
            }
        } else {
            status = s;
        }
        if (s == Status.UNHANDLED_EMERGENCY) {
            label.setForeground(Color.RED);
        }
        label.setText(status.name());
    }

    private void initializeEmergencySequence() {
        queue = new LinkedBlockingQueue<>();
        LastingClientHandler emergencyConnectionHandler = new LastingClientHandler(settings.getEmergencyService(), 4242, queue);
        emergencyConnectionHandler.start();
        sendMessageToQueue(new EmergencyMessage(EmergencyMessage.Action.OPEN, "New Emergency procedure started."));

        settings.getNeighboursAsList().forEach(this::alertNeighbour);
    }

    private void sendMessageToQueue(final Message message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void alertNeighbour(Neighbour neighbour) {
        new ClientHandler(neighbour.getAddress(), 8090, new Message()).start();
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

    public boolean validateRequestIp(String address) {
        return Optional.ofNullable(address).isPresent() &&
                (settings.getNeighboursAsList().stream().anyMatch(i -> i.getAddress().equals(address)) ||
                        address.equals(settings.getEmergencyService()) | ("127.0.0.1").equals(address));
    }

    public void sendNeighbourComing(LocalDateTime time, String ip) {
        settings.getNeighboursAsList()
                .stream()
                .filter(e -> e.getAddress().equals(ip))
                .findFirst()
                .ifPresent(e -> {
                    e.setComing(true);
                    settings.update(e);
                    sendMessageToQueue(new EmergencyMessage(EmergencyMessage.Action.UPDATE, ip + " coming at " + time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss"))));
                    setStatus(Status.HANDLED_EMERGENCY);
                });
    }

    public boolean openLock(LocalDateTime time) {
        Message message = new Message();
        new ClientHandler(settings.getLock(), 9090, message).start();

        return true;
    }
}
