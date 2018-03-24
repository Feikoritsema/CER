package message;

import status.BedSensorStatus;

public class BedMessage extends Message {
	private BedSensorStatus status;

	public BedSensorStatus getStatus() {
		return status;
	}

	public void setStatus(final BedSensorStatus status) {
		this.status = status;
	}
}
