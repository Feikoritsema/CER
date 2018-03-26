package hub;

import status.Status;

public interface QueueListener {
	void onStatusChange(Status s);
}
