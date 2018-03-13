public class Hub {

	public static void main(String args[]) throws Exception {
		System.out.println("I am the hub.");
		new DefaultQueue("localhost");
	}
}
