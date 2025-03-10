package org.example.request;

import java.util.Random;

/**
 * Generates random I/O requests following statistical distributions
 */
public class RandomRequestGenerator implements RequestGenerator {
    private final Random random = new Random();
    private final double meanInterarrivalTime;
    private final int maxPriority;
    private final int maxSize;
    private final int maxLocation;
    
    public RandomRequestGenerator(double meanInterarrivalTime, int maxPriority, 
                                 int maxSize, int maxLocation) {
        this.meanInterarrivalTime = meanInterarrivalTime;
        this.maxPriority = maxPriority;
        this.maxSize = maxSize;
        this.maxLocation = maxLocation;
    }
    
    @Override
    public Request generateRequest(double currentTime) {
        Request.RequestType type = random.nextDouble() < 0.7 ? 
                                  Request.RequestType.READ : Request.RequestType.WRITE;
        int priority = random.nextInt(maxPriority) + 1;
        int size = random.nextInt(maxSize) + 1;
        int location = random.nextInt(maxLocation);
        
        return new Request(type, priority, currentTime, size, location);
    }
    
    public double getNextArrivalTime() {
        // Exponential distribution for interarrival times
        return -meanInterarrivalTime * Math.log(random.nextDouble());
    }
}