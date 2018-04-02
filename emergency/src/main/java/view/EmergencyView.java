package view;

import model.Emergency;
import rest.RestClient;
import services.EmergencyHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EmergencyView extends JFrame implements PropertyChangeListener {

    private JLabel label;
    JLabel status;
    JTextArea log;
    JScrollPane scrollable;

    Emergency emergency;
    EmergencyHandler handler;

    public EmergencyView(EmergencyHandler handler) {
        super("Emergency @ " + handler.getEmergency().getHost());
        this.handler = handler;
        this.emergency = handler.getEmergency();
        this.emergency.addListener(this);
        buildGUI();
    }

    private void buildGUI() {
        setOnCloseOperation();

        LayoutManager layout = new BorderLayout();
        setLayout(layout);

        setPreferredSize(new Dimension(600, 800));
        JFrame.setDefaultLookAndFeelDecorated(true);
        setLocationRelativeTo(null);

        status = new JLabel("Status: Active");
        status.setFont(new Font("Serif", Font.PLAIN, 30));
        status.setPreferredSize(new Dimension(200, 100));
        add(status, BorderLayout.PAGE_START);

        log = new JTextArea();
        for (String message : emergency.getLog()) {
            log.append(message + "\n");
        }

        scrollable = new JScrollPane(log);
        scrollable.setPreferredSize(new Dimension(600, 700));
        add(scrollable, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.PAGE_END);

        pack();
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();

        LayoutManager layout = new BorderLayout();
        panel.setLayout(layout);

        JButton unlock = new JButton("Unlock door");
        unlock.setPreferredSize(new Dimension(200, 50));
        // TODO: Show some confirmation of unlocking
        unlock.addActionListener(e -> {
            RestClient restClient = new RestClient(emergency.getHost());
            String result = restClient.sendStringPostRequest("/lock");
            System.out.println(result);
        });

        panel.add(unlock, BorderLayout.LINE_START);

        return panel;
    }

    private void setOnCloseOperation() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) { }

            @Override
            public void windowClosing(WindowEvent e) { }

            @Override
            public void windowClosed(WindowEvent e) {
                handler.close();
            }

            @Override
            public void windowIconified(WindowEvent e) { }

            @Override
            public void windowDeiconified(WindowEvent e) { }

            @Override
            public void windowActivated(WindowEvent e) { }

            @Override
            public void windowDeactivated(WindowEvent e) { }
        });
    }

    public void display() {
        setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case "log":
                log.append((String) event.getNewValue() + "\n");
                break;
            case "status":
                if ((boolean) event.getNewValue()) { // when emergency is active
                    status.setText("Status: Active");
                } else {
                    status.setText("Status: Resolved");
                }
        }
    }
}
