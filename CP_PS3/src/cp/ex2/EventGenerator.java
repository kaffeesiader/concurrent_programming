package cp.ex2;

public interface EventGenerator {
	
	void registerListener(SafeEventListener listener);
	void infoListeningObjects();
}