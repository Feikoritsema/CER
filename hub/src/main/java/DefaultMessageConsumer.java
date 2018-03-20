import com.rabbitmq.client.Channel;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class DefaultMessageConsumer extends AbstractMessageConsumer {

	public static int LOW_AVG_HEARTRATE = 40;

	DefaultMessageConsumer(final Channel channel) {
		super(channel);
	}

	public Status updateStatus() {
		return deriveStatus();
	}

	private Status deriveStatus() {
		List<HeartrateMessage> heartrateMessages = getMessagesOf(HeartrateMessage.class);
		int idx = heartrateMessages.size() > 10 ? heartrateMessages.size() - MAX_ELEMENTS : 0;
		List<HeartrateMessage> subList = heartrateMessages.subList(idx, heartrateMessages.size());
		if (idx > 0) // clear or store unused messages?
			clearMessages(heartrateMessages.subList(0, idx));
		return getHeartRateStatus(subList);
	}

	private Status getHeartRateStatus(List<HeartrateMessage> heartrateMessages) {
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
