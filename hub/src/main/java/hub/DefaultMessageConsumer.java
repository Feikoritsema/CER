package hub;

import com.rabbitmq.client.Channel;
import message.BedMessage;
import message.HeartrateMessage;
import message.Message;
import message.MovementMessage;
import org.springframework.util.CollectionUtils;
import status.BedSensorStatus;
import status.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultMessageConsumer extends AbstractMessageConsumer {

    public static int LOW_AVG_HEARTRATE = 20;
    public static int NO_MOVEMENT_HOURS = 1;
    public static int HEARTRATE_INVALID_TIME_MIN = 1;
    public static int HEARTRATE_MINIMUM_MSG = 5;

    DefaultMessageConsumer(final Channel channel) {
        super(channel);
    }

    public Status updateStatus() {
        Status status = Status.OK;

        final List<HeartrateMessage> heartrateMessages = getLastNMessages(HeartrateMessage.class, MAX_ELEMENTS)
                .stream()
                .filter(DefaultMessageConsumer::isValidHeartrateMessage)
                .collect(Collectors.toList());

        final List<MovementMessage> movementMessages = getLastNMessages(MovementMessage.class, 10)
                .stream()
                .filter(DefaultMessageConsumer::isValidMovementMessage)
                .collect(Collectors.toList());

        final BedSensorStatus bedSensorStatus = getBedStatus();
        if (getMessagesOf(Message.class).size() > 0) {
            // Check heartrate
            if (hasHeartrateMessages(heartrateMessages)) {
                status = getHeartRateStatus(heartrateMessages);
            }
            // No movement & Not sleeping & No heartrate info
            else if (!hasMoved(movementMessages) && BedSensorStatus.NOT_ASLEEP.equals(bedSensorStatus)) {
                status = Status.UNHANDLED_EMERGENCY;
            }

            // BedSensor == ALARM
            if (BedSensorStatus.ALARM.equals(bedSensorStatus)) {
                status = Status.UNHANDLED_EMERGENCY;
            }
        }
        return status;
    }

    private BedSensorStatus getBedStatus() {
        List<BedMessage> bedMessages = getLastNMessages(BedMessage.class, MAX_ELEMENTS);
        if (!CollectionUtils.isEmpty(bedMessages))
            return bedMessages.get(0).getStatus();
        return BedSensorStatus.NOT_ASLEEP;
    }

    private boolean hasMoved(final List<MovementMessage> movementMessages) {
        // no recent movement messages or movement detected.
        return !(movementMessages.size() > 0) || movementMessages.stream().anyMatch(MovementMessage::getMovement);
    }

    private Status getHeartRateStatus(final List<HeartrateMessage> heartrateMessages) {
        if (heartrateMessages.size() >= HEARTRATE_MINIMUM_MSG) {
            Integer sum = 0;
            Integer avg;
            for (HeartrateMessage i : heartrateMessages)
                sum += i.getHeartrate();
            if ((avg = sum / heartrateMessages.size()) < LOW_AVG_HEARTRATE) {
                System.out.println("LOW AVERAGE HEARTRATE: " + avg.toString());
                return Status.UNHANDLED_EMERGENCY;
            }
        }
        return Status.OK;
    }

    private static boolean isValidMovementMessage(final MovementMessage movementMessage) {
        return movementMessage.getTime().isAfter(LocalDateTime.now().minusHours(NO_MOVEMENT_HOURS));
    }

    private static boolean isValidHeartrateMessage(HeartrateMessage heartrateMessage) {
        return heartrateMessage.getTime().isAfter(LocalDateTime.now().minusMinutes(HEARTRATE_INVALID_TIME_MIN));
    }

    private boolean hasHeartrateMessages(final List<HeartrateMessage> heartrateMessages) {
        return !CollectionUtils.isEmpty(heartrateMessages);
    }
}
