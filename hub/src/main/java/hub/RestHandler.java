package hub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import status.Status;

@RestController
@RequestMapping("/api")
public class RestHandler {

    private final Hub hub;

    @Autowired
    public RestHandler(Hub hub) {
        this.hub = hub;
    }

    // Endpoint for opening the lock
    @RequestMapping(value = "/lock", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> openLock(@RequestBody String user) {
        // Verify user who sent request
        System.out.println("Lock POST method called");
        if (user.equals("Feiko") || user.equals("EMERGENCYSERVICE")) {
            // Open Lock


            return new ResponseEntity<>("User: " + user + " unlocked the door.", HttpStatus.OK);
        }
        // Bad verification
        return new ResponseEntity<>("User: " + user + " NOT authorized.", HttpStatus.UNAUTHORIZED);
    }

    // Endpoint for marking the emergency as resolved
    @RequestMapping(value = "/emergency/neighbourComing", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> updateEmergency(@RequestBody String timestamp) {
        // Verify user who sent request
        System.out.println(timestamp);
        // Stop emergency (close em. socket etc.)
        System.out.println("Emergency PUT method called");
        return new ResponseEntity<>(timestamp + " Updated ", HttpStatus.OK);
    }

    // Endpoint for changing the settings
    @RequestMapping(value = "/settings", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> updateSettings() {
        System.out.println("Settings PUT method called");
        if (true) {
            // Update settings
            return new ResponseEntity<>("Settings updated", HttpStatus.OK);
        }

        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
    }


    // Testing endpoint
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> testMethod() {
        System.out.println("Test GET method called");
        String test = "Rest GET request for CER worked";
        hub.setStatus(Status.HANDLED_EMERGENCY);
        return new ResponseEntity<>(test, HttpStatus.OK);
    }

}
