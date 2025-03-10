package org.example.device;

import org.example.request.*;
import org.example.statistics.*;

/**
 * Simulates a printer with pages-per-minute speed
 */
public class PrinterDevice extends AbstractDevice {
    private final double pagesPerMinute;
    
    public PrinterDevice(String name, StatisticsCollector stats, double pagesPerMinute) {
        super(name, stats);
        this.pagesPerMinute = pagesPerMinute;
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
        // Assume size represents number of pages
        double printTimeMinutes = request.getSize() / pagesPerMinute;
        return currentTime + printTimeMinutes * 60; // Convert to seconds
    }
}