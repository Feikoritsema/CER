package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class Emergency {

    private int id;
    private int status;
    private ArrayList<String> log;

    private PropertyChangeSupport changes;

    public Emergency() {
        changes = new PropertyChangeSupport(this);
    }

    public void addListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    @SuppressWarnings (value="unchecked")
    public void addEvent(String event) {
        ArrayList<String> old = (ArrayList<String>) log.clone();
        log.add(event);
        changes.firePropertyChange("log", old, log);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        int old = status;
        this.status = status;
        changes.firePropertyChange("status", old, status);
    }

    public ArrayList<String> getLog() {
        return log;
    }
}
