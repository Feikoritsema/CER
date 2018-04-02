package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Emergency {

    private final String host;
    private boolean active;
    private LocalDateTime startedAt;
    private final ArrayList<String> log;

    private final PropertyChangeSupport changes;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        boolean old = this.active;
        this.active = active;
        changes.firePropertyChange("status", old, active);
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public void doorUnlocked() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String time = formatter.format(LocalDateTime.now());
        addEvent(time + ": Door unlocked.");
    }
}
