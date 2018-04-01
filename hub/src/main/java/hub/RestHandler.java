package hub;

import hub.settings.Neighbour;
import hub.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import status.Status;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class RestHandler {

    private final Hub hub;

    private final Settings settings;

    @Autowired
    public RestHandler(Hub hub, Settings settings) {
        this.hub = hub;
        this.settings = settings;
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
    @RequestMapping(value = "/emergency/neighbourComing", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> updateEmergency() {
        // Verify user who sent request

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        System.out.println("Sent from: " + request.getRemoteAddr());
        return new ResponseEntity<>(" Updated ", HttpStatus.OK);
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

    @PostMapping(value = "/settings/neighbour")
    @ResponseBody
    public ResponseEntity<?> createNeighbour(@RequestBody String address) {
        if (validate(address)) {
            final Neighbour neighbour = new Neighbour();
            neighbour.setAddress(address);
            neighbour.setComing(false);
            settings.addNeighbour(neighbour);
            return new ResponseEntity<>(address, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid address", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(value = "/settings/emergency_service")
    @ResponseBody
    public ResponseEntity<?> createEmergencyService(@RequestBody String address) {
        if (validate(address)) {
            settings.setEmergencyService(address);
            return new ResponseEntity<>(address, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid address", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //TODO extract to Util

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validate(final String ip) {
        return PATTERN.matcher(ip).matches();
    }
}
