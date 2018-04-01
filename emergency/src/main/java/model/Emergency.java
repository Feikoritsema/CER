package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class Emergency {

    private String host;
    private boolean active;
    private ArrayList<String> log;

    private PropertyChangeSupport changes;

    public Emergency() {
        this(null);
    }

    public Emergency(String host) {
        this.host = host;
        changes = new PropertyChangeSupport(this);
        log = new ArrayList<>();
    }

    public void addListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    public void addEvent(String event) {
        log.add(event);
        changes.firePropertyChange("log", null, event);
    }

    public ArrayList<String> getLog() {
        return log;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        boolean old = this.active;
        this.active = active;
        changes.firePropertyChange("status", old, active);
    }
}
