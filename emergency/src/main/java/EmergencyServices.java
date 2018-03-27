import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class EmergencyServices {
    private static final String URL = "http://localhost:8080/api";

    private EmergencyServices(){

    }

    public static void main(String args[]) {
        new EmergencyServices();

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
