package hub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class DefaultQueue extends Queue implements Runnable {

    private final static String CER_HUB_NORMAL = "CER_HUB_NORMAL";

    @Autowired
    DefaultQueue(@Value("${host:localhost}") final String host, Hub hub) {
        super(host, CER_HUB_NORMAL, hub);
        setConsumer(new DefaultMessageConsumer(getChannel()));
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            update(((DefaultMessageConsumer) getConsumer()).updateStatus());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
