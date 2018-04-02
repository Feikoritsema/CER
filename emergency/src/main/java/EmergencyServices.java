import rest.RestClient;

import javax.swing.*;

public class EmergencyServices {
    private RestClient restClient;
    private JLabel responseLabel;

    private EmergencyServices() {
        restClient = new RestClient();

        // UI part
        JFrame frame = new JFrame("EmergencyServices");
        frame.setVisible(true);
        frame.setSize(500, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        JButton openLock = new JButton("Open the lock");
        panel.add(openLock);
        openLock.addActionListener(e -> responseLabel.setText(restClient
                .sendStringPostRequest("/lock")));

        JButton sendOnWay = new JButton("Notify the ambulance is on its way.");
        panel.add(sendOnWay);
        sendOnWay.addActionListener(e -> responseLabel.setText(restClient.
                sendStringPostRequest("/emergency/emservicesComing")));

        JButton testMe = new JButton("TestMe");
        panel.add(testMe);
        testMe.addActionListener(e -> responseLabel.setText(restClient.sendGetRequest("/")));

        responseLabel = new JLabel("");
        panel.add(responseLabel);

    }

    public static void main(String args[]) {
        new EmergencyServices();
    }

}
