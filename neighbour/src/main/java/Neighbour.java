import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import message.*;

public class Neighbour extends Message {
    private  JLabel responseLabel;
    private  final String URL = "http://localhost:8080/api";

    private Neighbour(){
        System.out.println("I am the Neighbour.");

        // UI part
        JFrame frame = new JFrame("Test");
        frame.setVisible(true);
        frame.setSize(500,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        JButton openLock = new JButton("Open neighbour lock");
        panel.add(openLock);
        openLock.addActionListener (e -> sendStringPostRequest("/lock", "Feiko"));

        JButton sendOnWay = new JButton("Notify you're coming");
        panel.add(sendOnWay);
        sendOnWay.addActionListener (e -> sendStringPostRequest("/emergency/neighbourComing", getDateTime()));

        JButton testMe = new JButton("TestMe");
        panel.add(testMe);
        testMe.addActionListener (e -> sendGetRequest("/"));

        responseLabel = new JLabel("");
        panel.add(responseLabel);
    }

    public static void main(String args[]){
        new Neighbour();
    }

    private static String getDateTime(){
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

    private void sendGetRequest(String path){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> test = restTemplate.getForEntity(URL + path, String.class);
        String teststring = test.getBody();
        responseLabel.setText(teststring);
    }

    private void sendStringPostRequest(String path, String message){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(message);
        ResponseEntity<String> response = restTemplate.exchange(URL + path,
                HttpMethod.POST, request, String.class);
        String string = response.getBody();
        HttpStatus httpStatus = response.getStatusCode();
        assert(httpStatus.equals(HttpStatus.OK));
        responseLabel.setText(string);
    }

}
