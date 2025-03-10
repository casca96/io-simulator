package org.example.scheduler;

import java.util.ArrayList;
import java.util.List;
import org.example.device.*;
import org.example.request.*;

/**
 * SCAN (Elevator) scheduler for disk devices
 */
public class SCANScheduler implements Scheduler {
    private final String name;
    private final List<Request> queue = new ArrayList<>();
    private boolean movingUp = true;
    private int currentPosition = 0;
    
    public SCANScheduler(String name) {
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
        
        // Find next request in current direction
        Request selectedRequest = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Request request : queue) {
            int location = request.getLocation();
            
            if ((movingUp && location >= currentPosition) || 
                (!movingUp && location <= currentPosition)) {
                int distance = Math.abs(location - currentPosition);
                if (distance < minDistance) {
                    minDistance = distance;
                    selectedRequest = request;
                }
            }
        }
        
        // If no request found in current direction, change direction
        if (selectedRequest == null) {
            movingUp = !movingUp;
            return getNextRequest(device, currentTime);
        }
        
        // Update current position and remove selected request
        currentPosition = selectedRequest.getLocation();
        queue.remove(selectedRequest);
        
        return selectedRequest;
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