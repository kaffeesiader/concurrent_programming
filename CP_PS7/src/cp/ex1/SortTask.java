package cp.ex1;

import javax.swing.SwingWorker;

public class SortTask extends SwingWorker<String[], Object> {
	
	private int size;
	private String[] testSet;
	private String status;
	
	public SortTask(int size) {
		this.size = size;
		this.testSet = null;
		this.status = "Idle";
	}
	
	public String[] getTestset() {
		return testSet;
	}

	public String getStatus() {
		return status;
	}

	private void setStatus(String newStatus) {
		String old = status;
		status = newStatus;
		getPropertyChangeSupport().firePropertyChange("status", old, newStatus);
	}
	
	@Override
	protected String[] doInBackground() throws Exception {
		setStatus("Creating test set...");
		testSet = StringUtils.createTestSet(size);
		setStatus("Sorting...");
		getPropertyChangeSupport().firePropertyChange("testset", null, testSet);
		
		// create our helper array
		String[] temp = new String[size];
		// number of necessary iterations for progress estimation
		double itersTotal = (Math.log(size) / Math.log(2));
		int iters = 1, blockSize = 1;
	
		// uses the bottom-up approach
		while(blockSize < size && !isCancelled()) {
			
	        for(int low = 0; low < size - blockSize; low += blockSize*2) {
	            int mid = low + blockSize - 1;
	            int high = Math.min(low + (blockSize*2 - 1), size - 1);
	            
	            merge(testSet, temp, low, mid, high);
	        }
	        
	        int progress = (int)((iters/itersTotal) * 100);
	        setProgress(progress);
	        
	        iters++;
	        blockSize <<= 1;
		}
		
		return testSet;
	}
	
	@Override
	protected void done() {
		getPropertyChangeSupport().firePropertyChange("testset", testSet, testSet);
	}
			
	private void merge(String[] array, String[] temp, int lo, int mid, int hi) {
		// Copy both parts into the helper array
		for (int i = lo; i <= hi; i++) {
			temp[i] = array[i];
		}

		int i = lo;
		int j = mid + 1;
		int k = lo;
		// Copy the smallest values from either the left or the right side back
		// to the original array
		while (i <= mid && j <= hi) {
			if (temp[i].compareToIgnoreCase(temp[j]) <= 0) {
				array[k] = temp[i];
				i++;
			} else {
				array[k] = temp[j];
				j++;
			}
			k++;
		}
		// Copy the rest of the left side of the array into the target array
		while (i <= mid) {
			array[k] = temp[i];
			k++;
			i++;
		}
	}
}