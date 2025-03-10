package org.example;

import org.example.core.*;
import org.example.statistics.*;
import org.example.manager.*;
import org.example.request.*;
import org.example.scheduler.*;
import org.example.device.*;

public class IOSimulation {
    private final SimulationClock clock = new SimulationClock();
    private final StatisticsCollector stats = new StatisticsCollector();
    private final DeviceManager deviceManager = new DeviceManager(stats);
    
    /**
     * Run a simple disk simulation
     */
    public void runDiskSimulation(double simulationTime) {
        // Create devices
        DiskDevice disk = new DiskDevice("HDD1", stats, 150, 0.1, 4.2);
        
        // Create schedulers
        Scheduler fcfsScheduler = new FCFSScheduler("FCFS");
        Scheduler sstfScheduler = new SSTFScheduler("SSTF");
        Scheduler scanScheduler = new SCANScheduler("SCAN");
        
        // Register device with scheduler (choose one)
        deviceManager.registerDevice(disk, scanScheduler);
        
        // Create request generator
        RandomRequestGenerator generator = new RandomRequestGenerator(0.5, 3, 1024, 1000);
        
        // Schedule first request arrival
        double firstArrivalTime = generator.getNextArrivalTime();
        clock.scheduleEvent(new SimulationEvents.RequestArrivalEvent(
            firstArrivalTime, clock, generator, scanScheduler, deviceManager));
        clock.scheduleEvent(new SimulationEvents.DeviceCheckEvent(
            0, clock, scanScheduler, deviceManager));

        // Run simulation
        clock.run(simulationTime);
        
        // Print statistics
        stats.printSummary(simulationTime);
    }
    
    /**
     * Run a multi-device simulation
     */
    public void runMultiDeviceSimulation(double simulationTime) {
        // Create devices
        DiskDevice disk = new DiskDevice("HDD1", stats, 150, 0.1, 4.2);
        NetworkDevice network = new NetworkDevice("NET1", stats, 100, 10);
        PrinterDevice printer = new PrinterDevice("PRT1", stats, 10);
        
        // Create schedulers
        Scheduler diskScheduler = new SCANScheduler("SCAN");
        Scheduler networkScheduler = new PriorityScheduler("Priority");
        Scheduler printerScheduler = new FCFSScheduler("FCFS");
        
        // Register devices with schedulers
        deviceManager.registerDevice(disk, diskScheduler);
        deviceManager.registerDevice(network, networkScheduler);
        deviceManager.registerDevice(printer, printerScheduler);
        
        // Create request generators
        RandomRequestGenerator diskGenerator = new RandomRequestGenerator(0.5, 3, 1024, 1000);
        RandomRequestGenerator networkGenerator = new RandomRequestGenerator(0.2, 5, 512, 100);
        RandomRequestGenerator printerGenerator = new RandomRequestGenerator(2.0, 3, 20, 1);
        
        // Schedule first request arrivals
        clock.scheduleEvent(new SimulationEvents.RequestArrivalEvent(
            diskGenerator.getNextArrivalTime(), clock, diskGenerator, diskScheduler, deviceManager));
        clock.scheduleEvent(new SimulationEvents.RequestArrivalEvent(
            networkGenerator.getNextArrivalTime(), clock, networkGenerator, networkScheduler, deviceManager));
        clock.scheduleEvent(new SimulationEvents.RequestArrivalEvent(
            printerGenerator.getNextArrivalTime(), clock, printerGenerator, printerScheduler, deviceManager));
        
        // Run simulation
        clock.run(simulationTime);
        
        // Print statistics
        stats.printSummary(simulationTime);
    }
    
    /**
     * Main method to run the simulation
     */
    public static void main(String[] args) {
        IOSimulation simulation = new IOSimulation();
        
        System.out.println("Running disk simulation...");
        simulation.runDiskSimulation(100);
        
        System.out.println("\nRunning multi-device simulation...");
        simulation.runMultiDeviceSimulation(100);
    }
}