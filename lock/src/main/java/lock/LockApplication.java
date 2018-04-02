package lock;

import model.Lock;
import view.StatusView;

public class LockApplication {

    public final int PORT = 9090;

    private static Lock lock;
    private static StatusView view;

    public static void main(String args[]) throws Exception {
        lock = new Lock();
        view = new StatusView(lock);
    }
}
