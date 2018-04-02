import message.Message;
import rest.RestClient;
import tcp.ConnectionHandler;
import tcp.IpUtils;
import tcp.Server;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.format.DateTimeFormatter;

public class Neighbour extends JFrame implements Runnable {
    private RestClient restClient;
    private JLabel responseLabel;
    private JLabel emergencyStatus;
    private Server server;


    private Neighbour(String host) {
        System.out.println("I am the Neighbour.");
        restClient = new RestClient(host);
        server = new Server(8090);
        try {
            setTitle("Neighbour @ " + Server.findMachinesLocalIP().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            setTitle("Neighbour @ Unresolvable_host");

        }

        setPreferredSize(new Dimension(500, 100));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));
        JPanel panel = new JPanel();

        JButton openLock = new JButton("Open neighbour lock");
        panel.add(openLock);
        openLock.addActionListener(e -> responseLabel.setText(restClient
                .sendStringPostRequest("/lock").getBody()));

        JButton sendOnWay = new JButton("Notify you're coming");
        panel.add(sendOnWay);
        sendOnWay.addActionListener(e -> responseLabel.setText(restClient.
                sendStringPostRequest("/emergency/neighbour_coming").getBody()));

        emergencyStatus = new JLabel();
        emergencyStatus.setForeground(Color.RED);

        responseLabel = new JLabel();
        panel.add(responseLabel);
        add(panel);
        add(emergencyStatus);
        pack();
    }

    @Override
    public void run() {
        while (true) {
            try {
                final Socket clientSocket = server.waitForConnection();
                new ConnectionHandler(clientSocket) {
                    @Override
                    protected void handle(Message message) {
                        emergencyStatus.setText("Emergency situation, started at: " + message.getTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
                    }
                }.start();
            } catch (IOException e) {
                System.err.println("Cannot connect to client.");
                e.printStackTrace();
            }
        }
    }


    public static void main(String args[]) {
        if (args.length > 0 && IpUtils.validate(args[0])) {
            new Thread(new Neighbour(args[0])).start();
            System.out.println("Using address " + args[0]);
        } else {
            new Thread(new Neighbour("localhost")).start();
            System.out.println("Invalid address, assuming localhost");
        }
    }
}
