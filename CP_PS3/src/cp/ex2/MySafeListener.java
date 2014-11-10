package cp.ex2;

public class MySafeListener implements SafeEventListener {
	
	private final int id;
	
	private MySafeListener(int id) {
		this.id = id;
	}
	
	public void listenTo(EventGenerator generator) {	
		generator.registerListener(this);
	}
	
	public static MySafeListener newIntance(int id) {
		MySafeListener safe = new MySafeListener(id);
		return safe;
	}

	@Override
	public void handleEvent(MessageEvent e) {
		System.out.format("[Listener%d]: received message '%s'%n", id, e.getMessage());
	}
	
	@Override
	public String toString() {
		return String.format("[Listener%d]", id);
	}
	
}
