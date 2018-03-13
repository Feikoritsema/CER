public class Worker {

	public void handleMessage(Message m) {
		System.out.println("Default message");
	}

	public void handleMessage(HeartrateMessage m) {
		System.out.println("HeartrateMessage! heartrate: " + m.getHeartrate());
	}

	public void handleMessage(MovementMessage m) {
		System.out.println("Movement! moving?: " + m.getMovement());
	}
}
