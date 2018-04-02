import rest.RestClient;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Neighbour {
    private RestClient restClient;
    private JLabel responseLabel;


    private Neighbour(){
        System.out.println("I am the Neighbour.");
        restClient = new RestClient();
        // UI part
        JFrame frame = new JFrame("Neighbour");
        frame.setVisible(true);
        frame.setSize(500,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        JButton openLock = new JButton("Open neighbour lock");
        panel.add(openLock);
        openLock.addActionListener (e -> responseLabel.setText(restClient
                .sendStringPostRequest("/lock/")));

        JButton sendOnWay = new JButton("Notify you're coming");
        panel.add(sendOnWay);
        sendOnWay.addActionListener (e -> responseLabel.setText(restClient.
                sendStringPostRequest("/emergency/neighbourComing")));

        JButton testMe = new JButton("TestMe");
        panel.add(testMe);
        testMe.addActionListener (e -> responseLabel.setText(restClient.sendGetRequest("/")));

        responseLabel = new JLabel("");
        panel.add(responseLabel);
    }

    public static void main(String args[]){
        new Neighbour();
    }

    private static String getDateTime(){
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

}
