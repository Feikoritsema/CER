package view;

import model.Emergency;
import services.EmergencyHandler;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EmergencyView extends JFrame implements PropertyChangeListener {

    private JLabel label;

    EmergencyHandler handler;
    Emergency emergency;

    public EmergencyView(EmergencyHandler handler) {
        super("Emergency: " + handler.getEmergency().getId());
        this.handler = handler;
        this.emergency = handler.getEmergency();
        this.emergency.addListener(this);
        showGUI();
    }

    private void showGUI() {
        setPreferredSize(new Dimension(600, 200));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);
        setLocationRelativeTo(null);

        label = new JLabel("EmergencyId");
        label.setText(String.valueOf(this.handler.getId()));
        label.setFont(new Font("Serif", Font.PLAIN, 50));
        label.setPreferredSize(new Dimension(200, 200));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        // TODO: Implement!
        System.out.println(event.getPropertyName());
        System.out.println(event.getNewValue());
    }
}
