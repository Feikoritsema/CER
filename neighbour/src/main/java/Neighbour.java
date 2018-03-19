import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.xml.ws.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;

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
                sendOpenLock();
            } else if(command.equals("configureSettings")){
                sendConfigureSettings();
            } else if(command.equals("updateEmergency")){
                sendUpdateEmergency();
            } else if(command.equals("testGet")){
                sendGetRequest("/");
            }
        }

    }

    private static ResponseEntity<String> sendGetRequest(String path){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(URL + path, String.class);
        return response;
    }

    private static void sendPostRequest(String path){
        RestTemplate restTemplate = new RestTemplate();
        // Change String to corresponding serializable class / JSON
        HttpEntity<String> request = new HttpEntity<String>(new String("Test"));
        ResponseEntity<String> response = restTemplate.exchange(URL + path, HttpMethod.POST, request, String.class);
        String string = response.getBody();
    }

    private static SmartLock sendOpenLock(){
        RestTemplate restTemplate = new RestTemplate();
        // Change String to corresponding serializable class / JSON
        HttpEntity<SmartLock> request = new HttpEntity<SmartLock>(new SmartLock("Open"));
        ResponseEntity<SmartLock> response = restTemplate.exchange(URL + "/lock", HttpMethod.POST, request, SmartLock.class);
        SmartLock smartLock = response.getBody();
        return smartLock;
    }

}
