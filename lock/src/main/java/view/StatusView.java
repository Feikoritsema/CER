package view;

import model.Lock;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
        label = new JLabel();

        createLabel();

        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lock.toggle();
            }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });

        add(label);
        pack();
    }

    private void update() {
        createLabel();
    }

    private void createLabel() {
        if (lock.isLocked()) {
            if (lockedImage != null) {
                label.setIcon(new ImageIcon(lockedImage));
            } else {
                label.setText("Locked");
            }
        } else {
            if (unlockedImage != null) {
                label.setIcon(new ImageIcon(unlockedImage));
            } else {
                label.setText("Unlocked");
            }
        }
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
