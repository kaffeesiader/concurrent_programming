package cp.ex2;

public class MessageEvent {
	
	private final String message;
	
	public MessageEvent(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
