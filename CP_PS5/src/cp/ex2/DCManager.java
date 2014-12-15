package cp.ex2;

import java.util.PriorityQueue;
import java.util.Random;

class DCManager {
	
    private final int numberOfDataCenters = 4;
    private final int timeToSimulate = 50;
    private final int preloadTimeForJobsAtStart = 25;
    private final double timePerJob = 1; //every timePerJob time units a new job is added on average
    private final PriorityQueue<DataCenter> dcQueue = new PriorityQueue<DataCenter>();
    private final DataCenter[] dcArray = new DataCenter[numberOfDataCenters];
    private final Random rnd = new Random();



    public void run() {
        for(int i = 0; i < numberOfDataCenters; ++i) {
            DataCenter dc = new DataCenter(getGaussianDistributedRandom(0.01, 2, 1), "Center" + i); //computationPower with average around 2, minimum 0.01 and (theoretically) no maximum
            dcQueue.offer(dc); 
            dcArray[i] = dc;
        }
        double lastTime = -preloadTimeForJobsAtStart;
        while(true) {
            DataCenter cur = dcQueue.poll();
            double newTime = cur.getReadyTime();
            addNewJobs(lastTime, Math.min(timeToSimulate, newTime));
            if(newTime > timeToSimulate) {
                break;
            }
            lastTime = newTime;
            if(!cur.doNextOwnJob()) { //let's see if this datacenter can steal a job
                Job j = null;
                DataCenter stealFrom = null;
                for(DataCenter dc : dcArray) { //we even try to steal from ourself, but that doesn't matter, as we are broke ;)
                    Job potential = dc.peekAtPossibleSteal();
                    if(potential != null && (j == null || j.compareTo(potential) > 0)) {
                        j = potential;
                        stealFrom = dc;
                    }
                }
                if(j != null) {
                    cur.addJobToQueue(j);
                    stealFrom.stealJob(cur.getName());
                    cur.doNextOwnJob();
                } else {
                    cur.nothingToSteal();
                }
            }
            dcQueue.offer(cur);
        }

        System.out.println("\n-----------final standings----------");
        System.out.println("The simulation ran for " + timeToSimulate + " (+ " +  preloadTimeForJobsAtStart + " preload) time units");
        System.out.println(Job.finalStats());
        for(DataCenter dc : dcArray) {
            System.out.println(dc);
        }
    }

    public void addNewJobs(double from, double to) {
        double period = to - from;
        //System.out.println("period: " + period);
        assert(period >= 0);
        while(true) {
            double p = rnd.nextDouble();
            double quantil = - Math.log(1-p)*timePerJob;
            period -= quantil;
            if(period < 0) {
                return;
            }
            //create the new job
            double workLoad = getGaussianDistributedRandom(0.0001, 4, 2);
            Job j = new Job(workLoad, workLoad * getGaussianDistributedRandom(0.0001, 1, 0.3));
            dcArray[rnd.nextInt(numberOfDataCenters)].addJobToQueue(j);
        }
    }

    public static void main(String[] args) {
        DCManager dcm = new DCManager();
        dcm.run();
    }

    public double getGaussianDistributedRandom(double minimumValue, double approxAverage, double deviation) {
        return Math.max(minimumValue, approxAverage + deviation*rnd.nextGaussian());
    }
}
