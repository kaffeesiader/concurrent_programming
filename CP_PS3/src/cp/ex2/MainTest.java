package cp.ex2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainTest {
	
	public static void main(String[] args) throws InterruptedException {
		// create the two listeners
		MySafeListener listener0 = MySafeListener.newIntance(0);
		MySafeListener listener1 = MySafeListener.newIntance(1);
		
		List<EventGenerator> eventGenerators = Collections.synchronizedList(new ArrayList<EventGenerator>());
		ListeningObjectsThread lot = new ListeningObjectsThread(eventGenerators);
		lot.start();
		
		// create generators and add listeners
		EventGeneratorThread generator0 = new EventGeneratorThread(0);
		generator0.start();
		listener0.listenTo(generator0);
		eventGenerators.add(generator0);
		
		EventGeneratorThread generator1 = new EventGeneratorThread(1);
		generator1.start();
		listener1.listenTo(generator1);
		eventGenerators.add(generator1);
		
		// wait 10 secs before creating the other generators...
		Thread.sleep(10000);
		System.out.println("Adding 4 more generators...");
		
		Random rand = new Random();
		
		// create 4 more generators
		for (int i = 2; i < 6; i++) {
			EventGeneratorThread generator = new EventGeneratorThread(i);
			eventGenerators.add(generator);
			// add some random time difference...
			Thread.sleep(rand.nextInt(2000));
			generator.start();
		}
		// listener0 listens to generators 2, 3 and 4
		for (int i = 2; i < 5; i++) {
			listener0.listenTo(eventGenerators.get(i));
		}
		// listener0 listens to generators 3, 4 and 5
		for (int i = 3; i < 6; i++) {
			listener1.listenTo(eventGenerators.get(i));
		}
	}

}
