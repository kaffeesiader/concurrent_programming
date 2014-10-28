package uibk.cp.exercise2;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ComputePi {
	
	int stepSize;
	int precision;
	int maxSteps;
	
	Integer N;
	BigDecimal value;
	MathContext mc;
	
	boolean computed;
	
	public ComputePi(int stepSize, int precision) {
		this.computed = false;
		this.stepSize = stepSize;
		this.precision = precision;
		// compute number of steps, necessary to compute PI in the desired precision
		maxSteps = (int)(Math.pow(10, precision + 1) / 2);
		// compute necessary scale value based on precision
		int scale = (int)Math.log10(maxSteps) + 5;  // spend some extra decimal places
		this.mc = new MathContext(scale, RoundingMode.HALF_UP);
		
		this.value = new BigDecimal(0, mc);
		
//		System.out.println(maxSteps + " fractions necessary for computation");
	}
	
	public BigDecimal getPi() {
		if(!computed) {
			throw new IllegalStateException("Computation not finished yet!");
		} else {
			BigDecimal pi = value.multiply(new BigDecimal(4));
			// return value in the desired precision
			return pi.setScale(precision, BigDecimal.ROUND_HALF_UP);
		}
	}
	
	public void compute(ThreadPool tp) {
		int n = 0;
		
		while(n < maxSteps) {
			final int start = n;
			
			Runnable task = new Runnable() {
				public void run() {
					computeStep(start);
				}
			};
			
			tp.executeTask(task);
			
			n += stepSize;
		}
		// ensure that all threads complete...
		tp.shutdown();
		
		computed = true;
	}
	
	public void compute() {
		int n = 0;
		
		while(n < maxSteps) {
			computeStep(n);
			n += stepSize;
		}
		
		computed = true;
	}
	
	private void computeStep(int start) {
		// compute partial result for given range
		BigDecimal val = new BigDecimal(0.0, mc);
		for(int n = start; n < start + stepSize; ++n) {
			BigDecimal numerator = new BigDecimal(Math.pow(-1.0, n), mc);
			BigDecimal denominator = new BigDecimal(2.0 * n + 1.0, mc);
			BigDecimal tmp = numerator.divide(denominator, mc);
			
			val = val.add(tmp, mc);
		}
		// add partial result to current value
		addPartialResult(val);
	}
	
	private synchronized void addPartialResult(BigDecimal res) {
		this.value = this.value.add(res, mc);
	}
	
	public static void main(String[] args) {
		
		ComputePi cp = new ComputePi(100, 6);
		
		StopWatch watch = new StopWatch();
		
		watch.start();
		cp.compute();
		watch.stop();
		
		System.out.println("Result: " + cp.getPi());
		System.out.println("Computation took: " + watch);
	}

}
