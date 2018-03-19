package app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestHandler {

    // Endpoint for opening the lock
    @RequestMapping(value = "/lock", method = RequestMethod.POST)
    @ResponseBody
    public void openLock() {
        // Verify user who sent request

        // Open lock if verification worked
        System.out.println("POST method called");
    }

    // Endpoint for marking the emergency as resolved
    @RequestMapping(value = "/emergency", method = RequestMethod.PUT)
    @ResponseBody
    public void markResolved() {
        // Verify user who sent request

        // Stop emergency (close em. socket etc.)
        System.out.println("PUT method called");
    }

    // Endpoint for changing the settings
    @RequestMapping(value = "/settings", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> updateSettings() {
        if(true){
            // Update settings
            return new ResponseEntity<String>("Settings updated", HttpStatus.OK);
        }

        return new ResponseEntity<String>("Something went wrong", HttpStatus.BAD_REQUEST)
    }


    // Testing endpoint
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> testMethod() {
        String test = "Rest GET request for CER worked";
        return new ResponseEntity<String>(test, HttpStatus.OK);
    }

}
