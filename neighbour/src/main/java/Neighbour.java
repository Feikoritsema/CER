import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Neighbour {
    private static final String URL = "http://localhost:8080/api";

    public static void main(String args[]) throws java.io.IOException{

        System.out.println("I am the Neighbour.");

        String command;
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        while(true){
            System.out.print("Enter command: ");
            command = console.readLine();
            if(command.equals("openlock")){
                sendRestRequest("/lock", "POST");
            } else if(command.equals("configureSettings")){
                sendRestRequest("/settings", "PUT");
            } else if(command.equals("updateEmergency")){
                sendRestRequest("/emergency", "PUT");
            } else if(command.equals("testGet")){
                sendRestRequest2("/");
                sendRestRequest("/", "GET");
            }
        }


    }

    private static void sendRestRequest(String path, String method) {
        try {
            URL url = new URL(URL + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/xml");

            OutputStream os = connection.getOutputStream();
            os.flush();
            connection.getResponseCode();
            connection.disconnect();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendRestRequest2(String path){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(URL + path, String.class);
        System.out.println(response);

    }
}
