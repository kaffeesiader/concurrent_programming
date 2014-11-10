package cp.ex2;

import java.util.EventListener;

public interface SafeEventListener extends EventListener {
	
	void handleEvent(MessageEvent e);
	
}
