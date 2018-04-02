package view;

import model.Lock;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

public class StatusView extends JFrame implements PropertyChangeListener {

    private Lock lock;

    private URL lockedImage;
    private URL unlockedImage;

    private JLabel label;

    public StatusView(Lock lock) {
        this.lock = lock;
        lock.addListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ClassLoader classLoader = this.getClass().getClassLoader();
        lockedImage = classLoader.getResource("images/lock-closed.png");
        unlockedImage = classLoader.getResource("images/lock-open.png");

        buildGUI();
        setVisible(true);
    }

    private void buildGUI() {
        createLabel();
        pack();
    }

    private void update() {
        remove(label);
        createLabel();
    }

    private void createLabel() {
        if (lock.isLocked()) {
            if (lockedImage != null) {
                label = new JLabel(new ImageIcon(lockedImage));
            } else {
                label = new JLabel("Locked");
            }
        } else {
            if (unlockedImage != null) {
                label = new JLabel(new ImageIcon(unlockedImage));
            } else {
                label = new JLabel("Unlocked");
            }
        }

        add(label);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "locked":
                update();
                break;
        }
    }
}
