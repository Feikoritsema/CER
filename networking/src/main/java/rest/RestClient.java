package rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import message.Message;
import message.factories.JsonMessageFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClient {
    private String ip;
    private int port = 8080;
    private String base = "/api";
    private JsonMessageFactory jsonMessageFactory;

    public RestClient(String host){
        jsonMessageFactory = new JsonMessageFactory();
        ip = host;
    }

    public String sendGetRequest(String path){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://" + ip + ":" + port + base + path;
        ResponseEntity<String> response = restTemplate.getForEntity(url + path, String.class);
        return response.getBody();
    }

    public ResponseEntity<String> sendStringPostRequest(String path) {
        Message message = new Message();
        ResponseEntity<String> response = null;
        try {
            String json = jsonMessageFactory.messageToJson(message);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> request = new HttpEntity<>(json);
            String url = "http://" + ip + ":" + port + base + path;
            response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }
}
