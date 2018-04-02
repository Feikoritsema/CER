package tcp;

import com.fasterxml.jackson.core.JsonProcessingException;
import message.Message;
import message.factories.JsonMessageFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

class Client {

    private Socket socket;
    private DataOutputStream outputStream;

    private final JsonMessageFactory jsonMessageFactory;

    public Client() {
        jsonMessageFactory = new JsonMessageFactory();
    }

    public boolean connectTo(String host, int port) {
        try {
            socket = new Socket(host, port);
            outputStream = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException e) {
            System.err.println("Can't connect to host: " + host + " on port: " + port);
            e.printStackTrace();
        }

        return false;
    }

    public boolean send(Message message) {
        if (socket == null || !socket.isConnected()) {
            System.err.println("Can't send message because there is no connection with the host");
            return false;
        }

        PrintWriter pw = new PrintWriter(outputStream);
        try {
            String json = jsonMessageFactory.messageToJson(message);
            pw.write(json + "\n");
            pw.flush();
            return true;
        } catch (JsonProcessingException e) {
            System.err.println("Can't convert message to JSON");
            e.printStackTrace();
        }

        return false;
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
                System.out.println("Connection with " + socket.getInetAddress().getHostAddress() + " closed");
            }
        } catch (IOException e) {
            System.err.println("Can't close socket");
            e.printStackTrace();
        }
    }
}
