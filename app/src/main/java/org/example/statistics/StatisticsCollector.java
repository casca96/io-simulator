package org.example.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.device.*;
import org.example.request.*;

/**
 * Collects and analyzes performance statistics during simulation
 */
public class StatisticsCollector {
    private final Map<String, DeviceStatistics> deviceStats = new HashMap<>();
    
    /**
     * Inner class to store per-device statistics
     */
    class DeviceStatistics {
        private double totalBusyTime = 0;
        private int totalRequests = 0;
        private double totalWaitingTime = 0;
        private double totalServiceTime = 0;
        private double totalTurnaroundTime = 0;
        private final List<Request> completedRequests = new ArrayList<>();
        
        public void recordRequestStarted(Request request, double startTime) {
            totalRequests++;
        }
        
        public void recordRequestCompleted(Request request, double endTime) {
            completedRequests.add(request);
            totalWaitingTime += request.getWaitingTime();
            totalServiceTime += request.getServiceTime();
            totalTurnaroundTime += request.getTurnaroundTime();
        }
        
        public void updateBusyTime(double busyTime) {
            totalBusyTime += busyTime;
        }
        
        public double getAverageWaitingTime() {
            return completedRequests.isEmpty() ? 0 : totalWaitingTime / completedRequests.size();
        }
        
        public double getAverageTurnaroundTime() {
            return completedRequests.isEmpty() ? 0 : totalTurnaroundTime / completedRequests.size();
        }
        
        public double getDeviceUtilization(double totalSimTime) {
            return totalSimTime > 0 ? totalBusyTime / totalSimTime : 0;
        }
        
        public int getTotalCompletedRequests() {
            return completedRequests.size();
        }
    }
    
    /**
     * Register a device for statistics collection
     */
    public void registerDevice(Device device) {
        deviceStats.put(device.getName(), new DeviceStatistics());
    }
    
    /**
     * Record when a request starts processing
     */
    public void recordRequestStarted(Device device, Request request, double startTime) {
        DeviceStatistics stats = deviceStats.get(device.getName());
        if (stats != null) {
            stats.recordRequestStarted(request, startTime);
        }
    }
    
    /**
     * Record when a request completes processing
     */
    public void recordRequestCompleted(Device device, Request request, double endTime) {
        DeviceStatistics stats = deviceStats.get(device.getName());
        if (stats != null) {
            stats.recordRequestCompleted(request, endTime);
        }
    }
    
    /**
     * Update device busy time
     */
    public void updateDeviceBusyTime(Device device, double busyTime) {
        DeviceStatistics stats = deviceStats.get(device.getName());
        if (stats != null) {
            stats.updateBusyTime(busyTime);
        }
    }
    
    /**
     * Get performance metrics for a device
     */
    public Map<String, Object> getDeviceMetrics(Device device, double totalSimTime) {
        DeviceStatistics stats = deviceStats.get(device.getName());
        Map<String, Object> metrics = new HashMap<>();
        
        if (stats != null) {
            metrics.put("deviceName", device.getName());
            metrics.put("totalRequests", stats.totalRequests);
            metrics.put("completedRequests", stats.getTotalCompletedRequests());
            metrics.put("averageWaitingTime", stats.getAverageWaitingTime());
            metrics.put("averageTurnaroundTime", stats.getAverageTurnaroundTime());
            metrics.put("deviceUtilization", stats.getDeviceUtilization(totalSimTime));
        }
        
        return metrics;
    }
    
    /**
     * Print summary statistics for all devices
     */
    public void printSummary(double totalSimTime) {
        System.out.println("\n===== SIMULATION SUMMARY =====");
        System.out.println("Total simulation time: " + totalSimTime + " seconds");
        
        for (Map.Entry<String, DeviceStatistics> entry : deviceStats.entrySet()) {
            DeviceStatistics stats = entry.getValue();
            System.out.println("\nDevice: " + entry.getKey());
            System.out.println("  Total requests: " + stats.totalRequests);
            System.out.println("  Completed requests: " + stats.getTotalCompletedRequests());
            System.out.println("  Average waiting time: " + stats.getAverageWaitingTime() + " seconds");
            System.out.println("  Average turnaround time: " + stats.getAverageTurnaroundTime() + " seconds");
            System.out.println("  Device utilization: " + (stats.getDeviceUtilization(totalSimTime) * 100) + "%");
        }
    }
}