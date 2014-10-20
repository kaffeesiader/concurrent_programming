package uibk.cp.exercise2;

public class Main {

	public static void main(String[] args) {
		
		int stepSize = 100;
		int precision = 6;
		
		if(args.length > 0) {
			try {
				stepSize = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("Unable to parse provided stepSize parameter");
			}
		}
		
		if(args.length > 1) {
			try {
				precision = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("Unable to parse provided precision parameter");
			}
		}
		
		StopWatch watch = new StopWatch();
		
		System.out.format("Test parameters: stepSize=%d, precision=%d%n%n", stepSize, precision);
		
		for (int i = 1; i <= 20; i++) {
			
			System.out.format("Starting test run with %d threads...%n", i);
			ThreadPool tp = new ThreadPool(i);
			ComputePi cp = new ComputePi(stepSize, precision);
			
			watch.start();
			cp.compute(tp);
			watch.stop();
			
			System.out.format("Result: %s%n", cp.getPi());
			System.out.format("Computation took %d ms%n%n", watch.getDuration());
		}
		
		System.out.println("Test run completed!");
	}
	
}
