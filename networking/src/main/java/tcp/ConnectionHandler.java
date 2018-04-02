package tcp;

import message.Message;
import message.factories.JsonMessageFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public abstract class ConnectionHandler extends Thread {

    protected Socket socket;

    protected InputStream inputStream;

    JsonMessageFactory jsonMessageFactory;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
        jsonMessageFactory = new JsonMessageFactory();
    }

    protected abstract void handle(Message message);

    public void run() {
        try {
            inputStream = socket.getInputStream();
            Message message;
            while (!socket.isClosed()) {
                if ((message = receive()) != null) {
                    handle(message);
                } else {
                    sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            System.err.println("Thread couldn't sleep.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Message receive() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String json = reader.readLine();

        if (json != null && !"null".equals(json)) {
            System.out.println("Received from " + socket.getInetAddress().getHostAddress() + ": " + json);
            return jsonMessageFactory.jsonToMessage(json);
        }
        return null;
    }

    public void close() {
        try {
            if (!socket.isClosed()) {
                inputStream.close();
                socket.close();
                System.out.println("Connection with " + socket.getInetAddress().getHostAddress() + " closed.");
            }
        } catch (IOException e) {
            System.err.println("Could not close socket.");
        }
    }
}
