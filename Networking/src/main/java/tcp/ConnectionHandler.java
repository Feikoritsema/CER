package tcp;

import message.Message;
import message.factories.JsonMessageFactory;

import java.io.*;
import java.net.Socket;

public abstract class ConnectionHandler extends Thread {

    protected Socket socket;

    protected InputStream inputStream;
    protected OutputStream outputStream;

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
                Message message = receive();
                handle(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Message receive() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String json = reader.readLine();

        return jsonMessageFactory.jsonToMessage(json);
    }
}
