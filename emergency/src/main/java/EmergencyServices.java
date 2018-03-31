import message.Message;

import javax.swing.*;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmergencyServices {
    private static Server server;
    private RestClient restClient;
    private JLabel responseLabel;

    private EmergencyServices(){
        // UI part
        JFrame frame = new JFrame("EmergencyServices");
        frame.setVisible(true);
        frame.setSize(500,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        JButton openLock = new JButton("Open the lock");
        panel.add(openLock);
        openLock.addActionListener (e -> responseLabel.setText(restClient
                .sendStringPostRequest("/lock", "EmergencyServices")));

        JButton sendOnWay = new JButton("Notify the ambulance is on its way.");
        panel.add(sendOnWay);
        sendOnWay.addActionListener (e -> responseLabel.setText(restClient.
                sendStringPostRequest("/emergency/emservicesComing", getDateTime())));

        JButton testMe = new JButton("TestMe");
        panel.add(testMe);
        testMe.addActionListener (e -> responseLabel.setText(restClient.sendGetRequest("/")));

        responseLabel = new JLabel("");
        panel.add(responseLabel);

        server = new Server(4242);

        String ip = null;
        try {
            ip = Server.findMachinesLocalIP().toString();
            System.out.println("I am listening on: " + ip + ":4242");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        while (true) {
            server.waitForConnection();

            Message message;
            while (!server.isClosed()) { // TODO: Find out when to stop!
                message = server.receive();
//                System.out.print(message.getMessage());
            }
        }
    }

    public static void main(String args[]) {
        new EmergencyServices();
    }

    private static String getDateTime(){
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

}
