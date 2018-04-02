package hub.settings;

import org.springframework.stereotype.Component;

import javax.swing.*;

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
}
