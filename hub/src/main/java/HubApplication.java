public class HubApplication {

	public static void main(String args[]) {
		System.out.println("I am the hub.");
		new DefaultQueue("localhost");
	}
}
