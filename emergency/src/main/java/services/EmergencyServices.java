package services;

import message.EmergencyUpdateMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tcp.Server;

import java.net.UnknownHostException;

public class EmergencyServices {
    private static final String URL = "http://localhost:8080/api";

    private static Server server;

    public static void main(String args[]) {
        System.out.println("Emergency services control started.");

        server = new Server(4242);

        String ip = null;
        try {
            ip = Server.findMachinesLocalIP().toString();
            System.out.println("I am listening on: " + ip + ":4242");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        while (true) {
            server.waitForConnection();

            EmergencyUpdateMessage message;
            while (!server.isClosed()) { // TODO: Find out when to stop!
                message = (EmergencyUpdateMessage) server.receive();
                System.out.print(message.getUpdate());
            }
        }
    }

    private static void sendStringPostRequest(String path, String message){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(message);
        ResponseEntity<String> response = restTemplate.exchange(URL + path,
                HttpMethod.POST, request, String.class);
        String string = response.getBody();
        HttpStatus httpStatus = response.getStatusCode();
        assert(httpStatus.equals(HttpStatus.OK));
        System.out.println(string);
    }
}
