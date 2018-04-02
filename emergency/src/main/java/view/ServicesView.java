package view;

import model.Emergency;
import services.EmergencyServices;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServicesView extends JFrame {

    private JLabel title;
    private JTextArea emergencies;
    private JScrollPane scrollable;

    public ServicesView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildGUI();
    }

    private void buildGUI() {
        setPreferredSize(new Dimension(600, 600));
        JFrame.setDefaultLookAndFeelDecorated(true);
        setLocationRelativeTo(null);

        title = new JLabel("Emergencies");
        title.setFont(new Font("Serif", Font.PLAIN, 30));
        title.setPreferredSize(new Dimension(200, 100));
        add(title, BorderLayout.PAGE_START );

        emergencies = new JTextArea();
        fillEmergenciesField();

        scrollable = new JScrollPane(emergencies);
        scrollable.setPreferredSize(new Dimension(600, 500));
        add(scrollable, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    private String parseTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return formatter.format(time);
    }

    private void fillEmergenciesField() {
        for (Emergency emergency : EmergencyServices.getEmergencies()) {
            String entry = parseTime(emergency.getStartedAt()) + ": Emergency at " + emergency.getHost();
            if (!emergency.isActive()) {
                entry += " (resolved)";
            }

            emergencies.append(entry + "\n");
        }
    }

    public void update() {
        emergencies.setText("");
        fillEmergenciesField();
    }
}
