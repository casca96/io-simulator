package org.example.device;

import org.example.request.*;
import org.example.statistics.*;

/**
 * Simulates a disk device with mechanical constraints (seek time, rotational latency)
 */
public class DiskDevice extends AbstractDevice {
    private final double transferRate; // MB/s
    private final double seekTimeFactor; // ms per track difference
    private final double rotationalLatency; // ms
    private int currentHeadPosition = 0;
    
    public DiskDevice(String name, StatisticsCollector stats, 
                      double transferRate, double seekTimeFactor, double rotationalLatency) {
        super(name, stats);
        this.transferRate = transferRate;
        this.seekTimeFactor = seekTimeFactor;
        this.rotationalLatency = rotationalLatency;
    }
    
    @Override
    public void processRequest(Request request, double currentTime) {
        double completionTime = getEstimatedCompletionTime(request, currentTime);
        currentRequest = request;
        busyUntil = completionTime;
        
        request.setStartTime(currentTime);
        request.setCompletionTime(completionTime);
        
        // Update head position
        currentHeadPosition = request.getLocation();
        
        // Log statistics
        stats.recordRequestStarted(this, request, currentTime);
    }
    
    @Override
    public double getEstimatedCompletionTime(Request request, double currentTime) {
        // Calculate seek time based on head movement
        double seekTime = Math.abs(currentHeadPosition - request.getLocation()) * seekTimeFactor;
        
        // Add rotational latency (simplistic model)
        double totalLatency = seekTime + rotationalLatency;
        
        // Calculate transfer time
        double transferTime = request.getSize() / transferRate * 1000; // Convert to ms
        
        return currentTime + (totalLatency + transferTime) / 1000.0; // Convert back to seconds
    }
    
    public int getCurrentHeadPosition() {
        return currentHeadPosition;
    }
}