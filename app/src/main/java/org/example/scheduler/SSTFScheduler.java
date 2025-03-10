package org.example.scheduler;

import java.util.ArrayList;
import java.util.List;
import org.example.device.*;
import org.example.request.*;

/**
 * Shortest Seek Time First scheduler for disk devices
 */
public class SSTFScheduler implements Scheduler {
    private final String name;
    private final List<Request> queue = new ArrayList<>();
    
    public SSTFScheduler(String name) {
        this.name = name;
    }
    
    @Override
    public void addRequest(Request request) {
        queue.add(request);
    }
    
    @Override
    public Request getNextRequest(Device device, double currentTime) {
        if (queue.isEmpty()) {
            return null;
        }
        
        // Find request with minimal completion time
        Request bestRequest = null;
        double minCompletionTime = Double.MAX_VALUE;
        
        for (Request request : queue) {
            double completionTime = device.getEstimatedCompletionTime(request, currentTime);
            if (completionTime < minCompletionTime) {
                minCompletionTime = completionTime;
                bestRequest = request;
            }
        }
        
        if (bestRequest != null) {
            queue.remove(bestRequest);
        }
        
        return bestRequest;
    }
    
    @Override
    public int getQueueLength() {
        return queue.size();
    }
    
    @Override
    public String getName() {
        return name;
    }
}