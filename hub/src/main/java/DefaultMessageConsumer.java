import com.rabbitmq.client.Channel;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

public class DefaultMessageConsumer extends AbstractMessageConsumer {

	public static int LOW_AVG_HEARTRATE = 20;
	public static int NO_MOVEMENT_HOURS = 1;

	DefaultMessageConsumer(final Channel channel) {
		super(channel);
	}

	public Status updateStatus() {
		return deriveStatus();
	}

	private Status deriveStatus() {
		getBedStatus();
		return getMovementStatus();
	}

	private void getBedStatus() {

	}

	private Status getMovementStatus() {
		List<MovementMessage> movementMessages = getLastNMessages(MovementMessage.class, MAX_ELEMENTS);
		if (getHeartRateStatus() != Status.OK && movementMessages.size() > 0 &&
				movementMessages.get(movementMessages.size() - 1).getTime().isBefore(LocalDateTime.now().minusHours(NO_MOVEMENT_HOURS)))
			return Status.UNHANDLED_EMERGENCY;
		return Status.OK;
	}

	private Status getHeartRateStatus() {
		List<HeartrateMessage> heartrateMessages = getLastNMessages(HeartrateMessage.class, MAX_ELEMENTS);
		Integer sum = 0;
		Integer avg;
		if (!CollectionUtils.isEmpty(heartrateMessages)) {
			for (HeartrateMessage i : heartrateMessages)
				sum += i.getHeartrate();
			if ((avg = sum / heartrateMessages.size()) < LOW_AVG_HEARTRATE) {
				System.out.println("LOW AVERAGE HEARTRATE: " + avg.toString());
				return Status.UNHANDLED_EMERGENCY;
			}
		}
		return Status.OK;
	}
}
