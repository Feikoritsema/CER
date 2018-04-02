package tcp;

import message.Message;

public class ClientHandler extends Thread {
    private final Message message;
    private final int port;
    private final String host;

    public ClientHandler(final String host, int port, final Message message) {
        this.host = host;
        this.port = port;
        this.message = message;
    }

    @Override
    public void run() {
        Client client = new Client();
        int MAX_RETRIES = 3;
        int i = MAX_RETRIES;
        boolean isConnected = false;
        while (i > 0 && !(isConnected = client.connectTo(host, port))) {
            System.err.println("Could not connect to host. Attempt #" + (MAX_RETRIES - i + 1));
            i--;
        }
        if (isConnected) {
            client.send(message);
        }
        client.close();
    }
}
