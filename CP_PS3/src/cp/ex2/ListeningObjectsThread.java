package cp.ex2;

import java.util.ArrayList;
import java.util.List;

public class ListeningObjectsThread extends Thread {
	
	private List<EventGenerator> generators = new ArrayList<>();
	
	public ListeningObjectsThread(List<EventGenerator> generators) {
		this.generators = generators;
	}
	
	@Override
	public void run() {
		
		while(true) {
			for (EventGenerator gen : generators) {
				gen.infoListeningObjects();
			}
			try {
				sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println(this + " interrupted!");
				break;
			}
		}
	}
	
	@Override
	public String toString() {
		return "ListeningObjectsThread";
	}

}
