class DefaultQueue extends Queue {

	private final static String CER_HUB_NORMAL = "CER_HUB_NORMAL";

	DefaultQueue(final String host) {
		super(host, CER_HUB_NORMAL);
		DefaultMessageConsumer messageConsumer = new DefaultMessageConsumer(getChannel());
		setConsumer(messageConsumer);
	}

	@Override
	public void run() {
		while (true) {
			notifyListeners(((DefaultMessageConsumer) getConsumer()).updateStatus());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
