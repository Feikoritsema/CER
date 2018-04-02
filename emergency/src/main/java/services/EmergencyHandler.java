package services;

import message.EmergencyMessage;
import message.Message;
import model.Emergency;
import status.Status;
import tcp.ConnectionHandler;
import view.EmergencyView;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class EmergencyHandler extends ConnectionHandler {

    private Emergency emergency;
    private EmergencyView view;

    public EmergencyHandler(Socket socket) {
        super(socket);
        this.emergency = new Emergency(socket.getInetAddress().getHostAddress());
        this.view = new EmergencyView(this);
    }

    @Override
    protected void handle(Message message) {
        EmergencyMessage emergencyMessage = (EmergencyMessage) message;

        switch (emergencyMessage.getAction()) {
            case OPEN:
                emergency.addEvent(parseMessage(emergencyMessage));
                emergency.setActive(true);
                view.display();
                break;
            case UPDATE:
                emergency.addEvent(parseMessage(emergencyMessage));
                break;
            case CLOSE:
                close();
                emergency.addEvent(parseMessage(emergencyMessage));
                emergency.setActive(false);
                return;
            default:
                System.out.println("Unrecognized action.");
        }

    }

    private String parseMessage(EmergencyMessage message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String time = formatter.format(message.getTime());
        return time + ": " + message.getBody();
    }

    public Emergency getEmergency() {
        return emergency;
    }

    public void setEmergency(Emergency emergency) {
        this.emergency = emergency;
    }
}
