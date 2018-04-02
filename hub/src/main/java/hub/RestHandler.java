package hub;

import hub.settings.Neighbour;
import hub.settings.Settings;
import message.Message;
import message.factories.JsonMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import status.Status;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;

import static tcp.IpUtils.validate;

@RestController
@RequestMapping("/api")
public class RestHandler {

    private final Hub hub;
    private final Settings settings;
    private JsonMessageFactory jsonMessageFactory;

    @Autowired
    public RestHandler(Hub hub, Settings settings) {
        jsonMessageFactory = new JsonMessageFactory();
        this.hub = hub;
        this.settings = settings;
    }

    @RequestMapping(value = "/lock", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> openLock(@RequestBody String json) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String ip = request.getRemoteAddr();
        if (hub.validateNeighbour(ip)) {
            // TODO: Call open lock method
            try {
                LocalDateTime time = getTimeStamp(json);
                //hub.openLock(time,ip);
                return new ResponseEntity<>("Neighbour unlocked the door.", HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Bad verification
        return new ResponseEntity<>("NOT authorized.", HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/emergency/neighbour_coming", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> neighbourComing(@RequestBody String json) {
        // Verify user who sent request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String ip = request.getRemoteAddr();
        if (hub.validateNeighbour(ip)) {
            try {
                LocalDateTime time = getTimeStamp(json);
                // hub.sendNeighbourComing(time,ip);
                return new ResponseEntity<>("Updated", HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>("Unauthorized request ", HttpStatus.UNAUTHORIZED);
    }

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

    private LocalDateTime getTimeStamp(String json) throws IOException {
        Message message;
        message = jsonMessageFactory.jsonToMessage(json);
        LocalDateTime time = message.getTime();
        return time;
    }
}
