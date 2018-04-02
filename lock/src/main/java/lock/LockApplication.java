package lock;

import model.Lock;
import tcp.Server;
import view.StatusView;

import java.io.IOException;
import java.net.Socket;

public class LockApplication {

    private static final int PORT = 9090;

    public static void main(String args[]) throws Exception {
        Lock lock = new Lock();
        new StatusView(lock);

        Server server = new Server(PORT);

        System.out.println("Smart lock control started.");

        String ip = Server.findMachinesLocalIP();
        System.out.println("Smart lock listening on: " + ip + ":9090");

        while (true) {
            try {
                Socket clientSocket = server.waitForConnection();
                new LockHandler(clientSocket, lock).start();
            } catch (IOException e) {
                System.err.println("Cannot connect to client.");
                e.printStackTrace();
            }
        }
    }
}
