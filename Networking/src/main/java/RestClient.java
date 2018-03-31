import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClient {
    private final String URL = "http://localhost:8080/api";

    public String sendGetRequest(String path){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> test = restTemplate.getForEntity(URL + path, String.class);
        String teststring = test.getBody();
        return teststring;
    }

    public String sendStringPostRequest(String path, String message){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(message);
        ResponseEntity<String> response = restTemplate.exchange(URL + path,
                HttpMethod.POST, request, String.class);
        String string = response.getBody();
        HttpStatus httpStatus = response.getStatusCode();
        assert(httpStatus.equals(HttpStatus.OK));
        return string;
    }
}
