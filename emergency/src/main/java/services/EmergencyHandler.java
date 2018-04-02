package services;

import message.EmergencyMessage;
import message.Message;
import model.Emergency;
import status.Status;
import tcp.ConnectionHandler;
import view.EmergencyView;

import java.net.Socket;

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
                emergency.addEvent(emergencyMessage.getTime() + ": " + emergencyMessage.getBody());
                emergency.setActive(true);
                view.display();
                break;
            case UPDATE:
                emergency.addEvent(emergencyMessage.getTime() + ": " + emergencyMessage.getBody());
                break;
            case CLOSE:
                close();
                emergency.setActive(false);
                return;
            default:
                System.out.println("Unrecognized action.");
        }

    }

    public Emergency getEmergency() {
        return emergency;
    }

    public void setEmergency(Emergency emergency) {
        this.emergency = emergency;
    }
}
