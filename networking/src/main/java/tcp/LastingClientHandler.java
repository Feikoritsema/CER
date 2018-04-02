package tcp;

import message.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class LastingClientHandler extends Thread {
    private final String host;
    private final int port;
    private BlockingQueue<Message> queue;
    private Client client;
    private static int MAX_RETRIES = 3;
    private boolean isConnected;

    public LastingClientHandler(final String host, int port, final BlockingQueue<Message> queue) {
        this.host = host;
        this.port = port;
        this.queue = queue;
    }

    @Override
    public void run() {
        client = new Client();
        int i = MAX_RETRIES;
        isConnected = false;
        while (i > 0 && !(isConnected = client.connectTo(host, port))) {
            System.err.println("Could not connect to host. Attempt #" + (MAX_RETRIES - i + 1));
            i--;
        }
        while (isConnected && !isInterrupted()) {
            try {
                sendMessage(queue.poll(1, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        client.close();
    }

    public void close() {
        isConnected = false;
    }

    private void sendMessage(Message m) {
        client.send(m);
    }
}
