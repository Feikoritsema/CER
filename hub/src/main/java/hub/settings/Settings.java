package hub.settings;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;

@Component
public class Settings {
    private DefaultListModel<Neighbour> neighbours;
    private String emergencyService;
    private String lock;

    public Settings() {
        neighbours = new DefaultListModel<>();
    }

    public DefaultListModel<Neighbour> getNeighbours() {
        return neighbours;
    }

    public String getEmergencyService() {
        if (emergencyService == null || emergencyService.equals("")) {
            return "127.0.0.1";
        }
        return emergencyService;
    }

    public void setEmergencyService(String emergencyService) {
        this.emergencyService = emergencyService;
    }

    public void addNeighbour(Neighbour neighbour) {
        neighbours.addElement(neighbour);
    }

    public List<Neighbour> getNeighboursAsList() {
        return IteratorUtils.toList(neighbours.elements().asIterator());
    }

    public void update(Neighbour n) {
        neighbours.setElementAt(n, neighbours.indexOf(n));
    }

    public String getLock() {
        if (lock == null || lock.equals("")) {
            return "127.0.0.1";
        }
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }
}
