package hub.settings;

import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;

@Component
public class Settings {
    private DefaultListModel<Neighbour> neighbours;
    private String emergencyService;

    public Settings() {
        neighbours = new DefaultListModel<>();
    }

    public DefaultListModel<Neighbour> getNeighbours() {
        return neighbours;
    }

    public String getEmergencyService() {
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
}
