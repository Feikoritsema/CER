package rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import message.Message;
import message.factories.JsonMessageFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClient {
    private final String URL = "http://localhost:8080/api";
    private JsonMessageFactory jsonMessageFactory;

    public RestClient(){
        jsonMessageFactory = new JsonMessageFactory();
    }

    public String sendGetRequest(String path){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> test = restTemplate.getForEntity(URL + path, String.class);
        String teststring = test.getBody();
        return teststring;
    }

    public String sendStringPostRequest(String path) {
        Message message = new Message();
        String responseMessage = "";
        try {
            String json = jsonMessageFactory.messageToJson(message);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> request = new HttpEntity<>(json);
            ResponseEntity<String> response = restTemplate.exchange(URL + path,
                    HttpMethod.POST, request, String.class);
            responseMessage = response.getBody();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return responseMessage;
    }
}
