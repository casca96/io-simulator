package org.example.scheduler;

import java.util.Comparator;
import java.util.PriorityQueue;
import org.example.device.*;
import org.example.request.*;

/**
 * Priority-based scheduler implementation
 */
public class PriorityScheduler implements Scheduler {
    private final String name;
    private final PriorityQueue<Request> queue = new PriorityQueue<>(
            Comparator.comparingInt(Request::getPriority).reversed());
    
    public PriorityScheduler(String name) {
        this.name = name;
    }
    
    @Override
    public void addRequest(Request request) {
        queue.add(request);
    }
    
    @Override
    public Request getNextRequest(Device device, double currentTime) {
        return queue.poll();
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