package services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tcp.Server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class EmergencyServices {
    private static final String URL = "http://localhost:8080/api";

    private static Server server;

    public static void main(String args[]) {
        server = new Server(4242);

        System.out.println("Emergency services control started.");

        String ip;
        try {
            ip = Server.findMachinesLocalIP().toString();
            System.out.println("Emergency services listening on: " + ip + ":4242");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                Socket clientSocket = server.waitForConnection();
                new EmergencyHandler(clientSocket).start();
            } catch (IOException e) {
                System.err.println("Cannot connect to client.");
                e.printStackTrace();
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
