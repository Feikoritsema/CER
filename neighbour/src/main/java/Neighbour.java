import rest.RestClient;

import javax.swing.*;
import java.awt.*;

import static tcp.IpUtils.validate;

public class Neighbour extends JFrame {
    private RestClient restClient;
    private JLabel responseLabel;

    private Neighbour(String host) {
        System.out.println("I am the Neighbour.");
        restClient = new RestClient(host);
        
        setPreferredSize(new Dimension(500,100));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        JButton openLock = new JButton("Open neighbour lock");
        panel.add(openLock);
        openLock.addActionListener (e -> responseLabel.setText(restClient
                .sendStringPostRequest("/lock").getBody()));

        JButton sendOnWay = new JButton("Notify you're coming");
        panel.add(sendOnWay);
        sendOnWay.addActionListener (e -> responseLabel.setText(restClient.
                sendStringPostRequest("/emergency/neighbour_coming").getBody()));

        responseLabel = new JLabel(" ");
        panel.add(responseLabel);
        add(panel);
        pack();
    }

    public static void main(String args[]) {
        if (args.length > 0 && validate(args[0])) {
            new Neighbour(args[0]);
            System.out.println("Using address " + args[0]);
        } else {
            new Neighbour("localhost");
            System.out.println("Invalid address, assuming localhost");
        }
    }
}
