package cp.ex1;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

public class SortController implements PropertyChangeListener {
	
	private Gui gui;
	private SortTask currentTask;
	
	public SortController(Gui gui) {
		this.gui = gui;
	}
	
	// called only from UI thread
	public void startTask(int size) {
		if(currentTask != null)
			throw new IllegalStateException("Task already running!");
		
		System.out.println("Starting new sort task of size " + size);
		currentTask = new SortTask(size);
		currentTask.addPropertyChangeListener(this);
		currentTask.execute();
		
		gui.displayArray(new String[0]);
		gui.setBusy(true);
	}
	
	// called only from UI thread
	public void cancelTask() {
		if(currentTask == null)
			throw new IllegalStateException("No task to cancel...");
		
		System.out.println("Cancelling current task.");
		currentTask.cancel(true);
	}
	
	private void handleTaskFinished() {
		if(currentTask.isCancelled()) {
			gui.reportProgress(0);
			gui.setStatus("Task cancelled!");
			gui.displayArray(new String[0]);
			System.out.println("Task cancelled!");
		} else {
			gui.reportProgress(100);
			gui.setStatus("Task completed!");
			gui.displayArray(getDisplayData(currentTask));
			System.out.println("Task completed!");
		}
		currentTask.removePropertyChangeListener(this);
		currentTask = null;
		gui.setBusy(false);
	}
	
	private String[] getDisplayData(SortTask task) {
		String[] testSet = task.getTestset();
		// display only a part of the test set...
		return Arrays.copyOfRange(testSet, 0, Math.min(500, testSet.length));
	}

	@Override
	public void propertyChange(PropertyChangeEvent ev) {
		switch(ev.getPropertyName()) {
		case "progress":
			gui.reportProgress(currentTask.getProgress());
			break;
		case "status":
			gui.setStatus(currentTask.getStatus());
			break;
		case "state":
			if(currentTask.isDone())
				handleTaskFinished();
			break;
		case "testset":
			gui.displayArray(getDisplayData(currentTask));
			break;
		}
	}
}
