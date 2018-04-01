package tcp;

import message.Message;
import message.factories.JsonMessageFactory;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

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
            while (!socket.isClosed()) {
                try {
                    Message message = receive();
                    handle(message);
                    sleep(1000);
                } catch (NullPointerException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    System.err.println("Thread couldn't sleep.");
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Message receive() throws IOException, NullPointerException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String json = reader.readLine();

        if (json == null) {
            throw new NullPointerException("Read null from socket.");
        }

        System.out.println("Received from " + socket.getInetAddress().getHostAddress() + ": " + json);

        return jsonMessageFactory.jsonToMessage(json);
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
