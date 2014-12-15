package cp.ex2;

class Job implements Comparable<Job> {
	
    private final double workSize;
    private final double rewardAmount;
    private final double jobValue; //this is always rewardAmount / workSize
    private static int numberJobsCreated = 0;
    private static int numberJobsCompleted = 0;

    public Job(double workSize, double rewardAmount) {
        this.workSize = workSize;
        this.rewardAmount = rewardAmount;
        this.jobValue = workSize / rewardAmount;
        ++numberJobsCreated;
    }

    public double getWorkSize() {
        return workSize;
    }

    public double getRewardAmount() {
        return rewardAmount;
    }
    
    public double getJobValue() {
        return jobValue;
    }

    public void jobCompleted() {
        ++numberJobsCompleted;
    }

    public static String finalStats() {
        return "JobsCreated: " + numberJobsCreated + ", JobsCompleted: " + numberJobsCompleted;
    }

    public String toString() {
        return "Job (workSize = " + String.format("%7.4f", workSize) + ", rewardAmount = " + String.format("%7.4f", rewardAmount) + ")";
    }

	@Override
	public int compareTo(Job anotherJob) {
        double our = this.jobValue;
        double their = anotherJob.jobValue;
        
        if(our < their) 
            return -1;
        
        if(our > their)
            return 1;
        
        return 0;
	}
}
