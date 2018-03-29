import com.fasterxml.jackson.core.JsonProcessingException;
import message.Message;
import message.factories.JsonMessageFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    protected Socket socket;
    protected DataOutputStream outputStream;

    JsonMessageFactory jsonMessageFactory;

    public Client() {
        jsonMessageFactory = new JsonMessageFactory();
    }

    public void connectTo(String host, int port) {
        try {
            socket = new Socket(host, port);
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Can't connect to host: " + host + " on port: " + port);
            e.printStackTrace();
        }
    }

    public void send(Message message) {
        PrintWriter pw = new PrintWriter(outputStream);
        try {
            String json = jsonMessageFactory.messageToJson(message);
            pw.write(json);
        } catch (JsonProcessingException e) {
            System.err.println("Can't convert message to JSON");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Can't close socket");
            e.printStackTrace();
        }
    }
}
