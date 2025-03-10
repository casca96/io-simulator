package org.example.scheduler;

import java.util.LinkedList;
import java.util.Queue;
import org.example.device.*;
import org.example.request.*;

/**
 * First-Come-First-Served scheduler implementation
 */
public class FCFSScheduler implements Scheduler {
    private final String name;
    private final Queue<Request> queue = new LinkedList<>();
    
    public FCFSScheduler(String name) {
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