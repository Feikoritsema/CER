package rest;

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

    public RestClient(String host) {
        jsonMessageFactory = new JsonMessageFactory();
        ip = host;
    }

    public ResponseEntity<String> sendStringPostRequest(String path) {
        Message message = new Message();
        ResponseEntity<String> response;
        try {
            String json = jsonMessageFactory.messageToJson(message);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> request = new HttpEntity<>(json);
            String url = "http://" + ip + ":" + port + base + path;
            response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error sending request", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
