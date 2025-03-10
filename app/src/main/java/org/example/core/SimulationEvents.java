package org.example.core;

import org.example.request.*;
import org.example.scheduler.*;
import org.example.device.*;
import org.example.statistics.*;
import org.example.manager.*;

public class SimulationEvents {
    
    /**
     * Event for request arrival
     */
    public static class RequestArrivalEvent extends AbstractEvent {
        private final RequestGenerator generator;
        private final Scheduler scheduler;
        private final DeviceManager deviceManager;
        
        public RequestArrivalEvent(double scheduledTime, SimulationClock clock, 
                                 RequestGenerator generator, Scheduler scheduler, DeviceManager deviceManager) {
            super(scheduledTime, clock);
            this.generator = generator;
            this.scheduler = scheduler;
            this.deviceManager = deviceManager;
        }
        
        @Override
        public void execute() {
            // Generate the request
            Request request = generator.generateRequest(scheduledTime);
            
            // Add to scheduler queue
            scheduler.addRequest(request);
            
            // Schedule next arrival if using RandomRequestGenerator
            if (generator instanceof RandomRequestGenerator) {
                RandomRequestGenerator randomGen = (RandomRequestGenerator) generator;
                double nextArrivalTime = scheduledTime + randomGen.getNextArrivalTime();
                clock.scheduleEvent(new RequestArrivalEvent(nextArrivalTime, clock, generator, scheduler, deviceManager));
            }
            
            // Schedule device check event
            clock.scheduleEvent(new DeviceCheckEvent(scheduledTime, clock, scheduler, deviceManager));
        }
    }
    
    /**
     * Event to check device status
     */
    public static class DeviceCheckEvent extends AbstractEvent {
        private final Scheduler scheduler;
        private final DeviceManager deviceManager;
        
        public DeviceCheckEvent(double scheduledTime, SimulationClock clock, 
                            Scheduler scheduler, DeviceManager deviceManager) {
            super(scheduledTime, clock);
            this.scheduler = scheduler;
            this.deviceManager = deviceManager;
        }
        
        @Override
        public void execute() {
            // Check devices and process queued requests
            deviceManager.checkDevices(clock, scheduledTime);
        }
    }    

    /**
     * Event for request completion
     */
    public static class RequestCompletionEvent extends AbstractEvent {
        private final Device device;
        private final Request request;
        private final Scheduler scheduler;
        private final StatisticsCollector stats;
        
        public RequestCompletionEvent(double scheduledTime, SimulationClock clock, 
                                    Device device, Request request, 
                                    Scheduler scheduler, StatisticsCollector stats) {
            super(scheduledTime, clock);
            this.device = device;
            this.request = request;
            this.scheduler = scheduler;
            this.stats = stats;
        }
        
        @Override
        public void execute() {
            // Record statistics
            stats.recordRequestCompleted(device, request, scheduledTime);
            
            // Mark device as available by resetting currentRequest
            // This assumes all devices inherit from AbstractDevice
            if (device instanceof AbstractDevice) {
                ((AbstractDevice)device).completeCurrentRequest();
            }
            
            // Process next request from queue if available
            Request nextRequest = scheduler.getNextRequest(device, scheduledTime);
            if (nextRequest != null) {
                device.processRequest(nextRequest, scheduledTime);
                
                // Schedule completion event for this request
                double completionTime = device.getEstimatedCompletionTime(nextRequest, scheduledTime);
                clock.scheduleEvent(new RequestCompletionEvent(
                    completionTime, clock, device, nextRequest, scheduler, stats));
            }
        }
    }
}