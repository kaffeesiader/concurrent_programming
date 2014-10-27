package cp.ps2.ex2;

import java.util.Map;
import java.util.TreeMap;

public class Consumer extends Thread {

	private Buffer buffer;
	private boolean completed[];
	
	public Consumer(Buffer buffer) {
		this.buffer = buffer;
		completed = new boolean[4];
	}
	
	@Override
	public void run() {

		while(true) {
			Map<Integer, Integer> vals = new TreeMap<Integer, Integer>();
			
			for (int i = 0; i < buffer.size(); i++) {
				if(!completed[i]) {
					int value = buffer.get(i);
					vals.put(i, value);
					completed[i] = (value == 0);
				}
			}
			
			StringBuilder sb = new StringBuilder("[");
			for (Integer key : vals.keySet()) {
				sb.append(String.format(" %d=%d", key, vals.get(key)));
			}
			sb.append(" ]");
			
			System.out.println(String.format("[Consumer] received values %s from buffer.", sb.toString()));
			if(allCompleted()) {
				break;
			}
			
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				System.out.println("[Consumer] interrupted!");
				break;
			}
		}
		
		System.out.println("[Consumer] shut down!");
	}

	private boolean allCompleted() {
		for (int i = 0; i < completed.length; i++) {
			if(!completed[i]) {
				return false;
			}
		}
		return true;
	}
}
