package uibk.cp.exercise2;

public class StopWatch {
	
	private boolean started;
	
	private long startTime;
	private long stopTime;
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public void stop() {
		stopTime = System.currentTimeMillis();
	}
	
	public long getDuration() {
		return stopTime - startTime;
	}
	
	@Override
	public String toString() {
		if(started) {
			return "still running...";
		} else {
			return getDuration() + "ms";
		}
	}
 
}
