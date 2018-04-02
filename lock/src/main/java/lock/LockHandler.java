package lock;

import message.Message;
import model.Lock;
import tcp.ConnectionHandler;

import java.net.Socket;

public class LockHandler extends ConnectionHandler {

    private Lock lock;

    public LockHandler(Socket socket, Lock lock) {
        super(socket);
        this.lock = lock;
    }

    @Override
    protected void handle(Message message) {
        lock.setLocked(false);
        close();
        currentThread().interrupt();
    }
}
