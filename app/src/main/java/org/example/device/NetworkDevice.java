package org.example.device;

import org.example.request.*;
import org.example.statistics.*;

/**
 * Simulates a network interface with bandwidth and latency constraints
 */
public class NetworkDevice extends AbstractDevice {
    private final double bandwidth; // MB/s
    private final double latency; // ms
    
    public NetworkDevice(String name, StatisticsCollector stats, double bandwidth, double latency) {
        super(name, stats);
        this.bandwidth = bandwidth;
        this.latency = latency;
    }
    
    @Override
    public void processRequest(Request request, double currentTime) {
        double completionTime = getEstimatedCompletionTime(request, currentTime);
        currentRequest = request;
        busyUntil = completionTime;
        
        request.setStartTime(currentTime);
        request.setCompletionTime(completionTime);
        
        // Log statistics
        stats.recordRequestStarted(this, request, currentTime);
    }
    
    @Override
    public double getEstimatedCompletionTime(Request request, double currentTime) {
        // Calculate transfer time plus network latency
        double transferTime = request.getSize() / bandwidth * 1000; // Convert to ms
        return currentTime + (latency + transferTime) / 1000.0; // Convert to seconds
    }
}