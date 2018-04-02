package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Lock {

    private boolean locked;
    private PropertyChangeSupport changes;

    public Lock() {
        this(true);
    }

    public Lock(boolean locked) {
        this.locked = locked;
        changes = new PropertyChangeSupport(this);
    }

    public void addListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;

        if (locked) {
            System.out.println("The door has been locked.");
        } else {
            System.out.println("The door has been unlocked.");
        }

        changes.firePropertyChange("locked", null, locked);
    }

    public void toggle() {
        setLocked(!locked);
    }
}
