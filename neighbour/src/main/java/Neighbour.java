import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Neighbour {
    private static final String URL = "http://localhost:8080/api";

    public static void main(String args[]){


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
        openLock.addActionListener (new OpenLock());

        JButton sendOnWay = new JButton("Notify you're coming");
        panel.add(sendOnWay);
        sendOnWay.addActionListener (new sendOnWay());

        JButton testMe = new JButton("TestMe");
        panel.add(testMe);
        testMe.addActionListener (new test());


    }
    static class test implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println(sendGetRequest("/"));
        }
    }

    static class OpenLock implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            sendStringPostRequest("/lock", "Feiko");
        }
    }

    static class sendOnWay implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            sendStringPostRequest("/emergency/neighbourComing", timeStamp);
        }
    }

    private static ResponseEntity<String> sendGetRequest(String path){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(URL + path, String.class);
    }

    // TODO: Determine if only String post requests suffice in stead of JSON / serializable classes

    private static void sendStringPostRequest(String path, String message){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(message);
        ResponseEntity<String> response = restTemplate.exchange(URL + path,
                HttpMethod.POST, request, String.class);
        String string = response.getBody();
        HttpStatus httpStatus = response.getStatusCode();
        assert(httpStatus.equals(HttpStatus.OK));
        System.out.println(string);
    }

}
