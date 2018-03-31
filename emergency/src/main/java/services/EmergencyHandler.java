package services;

import message.EmergencyMessage;
import message.Message;
import model.Emergency;
import tcp.ConnectionHandler;

import java.net.Socket;

public class EmergencyHandler extends ConnectionHandler {

    private Emergency emergency;

    public EmergencyHandler(Socket socket) {
        super(socket);
        this.emergency = new Emergency();
    }

    @Override
    protected void handle(Message message) {
        EmergencyMessage emergencyMessage = (EmergencyMessage) message;
        System.out.println(emergencyMessage.getAction());
        System.out.println(emergencyMessage.getBody());
    }

    public Emergency getEmergency() {
        return emergency;
    }

    public void setEmergency(Emergency emergency) {
        this.emergency = emergency;
    }
}
