package services;

import model.Emergency;
import tcp.Server;
import view.ServicesView;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class EmergencyServices {
    private static Server server;

    private static ServicesView view;
    private static ArrayList<Emergency> emergencies;

    public static void main(String args[]) {
        server = new Server(4242);

        System.out.println("Emergency services control started.");

        String ip = Server.findMachinesLocalIP();
        System.out.println("Emergency services listening on: " + ip + ":4242");

        emergencies = new ArrayList<>();
        view = new ServicesView();

        while (true) {
            try {
                Socket clientSocket = server.waitForConnection();
                new EmergencyHandler(clientSocket).start();
            } catch (IOException e) {
                System.err.println("Cannot connect to client.");
                e.printStackTrace();
            }
        }
    }

    public static void addEmergency(Emergency emergency) {
        emergencies.add(emergency);
        view.update();
    }

    public static void update() {
        view.update();
    }

    public static ArrayList<Emergency> getEmergencies() {
        return emergencies;
    }
}
