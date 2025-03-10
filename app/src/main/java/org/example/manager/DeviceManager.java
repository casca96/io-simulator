package org.example.manager;

import java.util.HashMap;
import java.util.Map;
import org.example.device.*;
import org.example.scheduler.*;
import org.example.statistics.*;
import org.example.request.*;
import org.example.core.*;

/**
 * Manages devices and their associated schedulers
 */
public class DeviceManager {
    private final Map<Device, Scheduler> deviceSchedulers = new HashMap<>();
    private final StatisticsCollector stats;
    
    public DeviceManager(StatisticsCollector stats) {
        this.stats = stats;
    }
    
    /**
     * Register a device with its scheduler
     */
    public void registerDevice(Device device, Scheduler scheduler) {
        deviceSchedulers.put(device, scheduler);
        stats.registerDevice(device);
    }
    
    /**
     * Process a request for a specific device
     */
    public void processRequest(Device device, Request request, double currentTime) {
        Scheduler scheduler = deviceSchedulers.get(device);
        if (scheduler != null) {
            if (device.isIdle()) {
                // Device is idle, process request immediately
                device.processRequest(request, currentTime);
            } else {
                // Device is busy, add to scheduler queue
                scheduler.addRequest(request);
            }
        }
    }
    
    /**
     * Check all devices and process queued requests if devices are idle
     */
    public void checkDevices(SimulationClock clock, double currentTime) {
        for (Map.Entry<Device, Scheduler> entry : deviceSchedulers.entrySet()) {
            Device device = entry.getKey();
            Scheduler scheduler = entry.getValue();
            
            if (device.isIdle()) {
                Request nextRequest = scheduler.getNextRequest(device, currentTime);
                if (nextRequest != null) {
                    device.processRequest(nextRequest, currentTime);
                    
                    // Schedule completion event
                    double completionTime = device.getEstimatedCompletionTime(nextRequest, currentTime);
                    clock.scheduleEvent(new SimulationEvents.RequestCompletionEvent(
                        completionTime, clock, device, nextRequest, scheduler, stats));
                }
            }
        }
    }
}