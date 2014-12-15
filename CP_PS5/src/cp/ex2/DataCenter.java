package cp.ex2;

import java.util.concurrent.LinkedBlockingDeque;

class DataCenter implements Comparable<DataCenter> {
	
    private final double computationPower;
    private final LinkedBlockingDeque<Job> jobQueue = new LinkedBlockingDeque<Job>();
    private final String name;
    private double readyTime; //the next time this DataCenter can be active again (i.e. when currently running job is done)
    private double totalEarned = 0;
    private Job currentJob = null;
    private long idleChunks = 0;
    private static final double idleChunkSize = 0.01;

    public DataCenter(double computationPower, String name) {
        this.computationPower = computationPower;
        this.name = name;
        readyTime = 0; //I was born ready!
    }

    public double getReadyTime() {
        return readyTime;
    }

    public boolean doNextOwnJob() {
        if(currentJob != null) {
            totalEarned += currentJob.getRewardAmount();
            currentJob.jobCompleted();
            currentJob = null;
        }
        if(jobQueue.isEmpty()) {
            return false;
        }
        try {
            executeJob(jobQueue.take());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void nothingToSteal() {
        readyTime += idleChunkSize;
        ++idleChunks;
    }

    public void executeJob(Job j) {
        assert(currentJob == null);
        currentJob = j;
        readyTime += j.getWorkSize() / computationPower;
        System.out.println(this + " doing " + j);
    }

    public void addJobToQueue(Job j) {
        //System.out.println(this + " got new: " + j);
        try {
            jobQueue.put(j);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Job peekAtPossibleSteal() {
        return jobQueue.peekLast();
    }

    public void stealJob(String stealerName) {
        Job j = jobQueue.pollLast();
        assert(j != null);
        System.out.println(stealerName + " stole from " + this + ": " + j);
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name + " (earnings = " + String.format("%6.2f", totalEarned) + ", power = " + String.format("%4.2f", computationPower) + ", idleTime = " + String.format("%5.2f", idleChunks * idleChunkSize) + ", jobDequeSize = " + jobQueue.size() + (currentJob != null ? " + 1 active" : "") + ")";
    }

	@Override
	public int compareTo(DataCenter anotherDataCenter) {
        double our   = this.readyTime;
        double their = ((DataCenter)anotherDataCenter).readyTime;
        if(our < their) {
            return -1;
        }
        if(our > their) {
            return 1;
        }
        return 0;
	}
}
