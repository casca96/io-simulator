# IO Simulator

A discrete event-driven simulation framework for modeling and analyzing I/O device scheduling algorithms and performance metrics.

## Overview

This project provides a comprehensive simulation environment for studying how different I/O scheduling algorithms perform under various workloads. It models real-world I/O devices with their physical constraints and collects detailed statistics on system performance.

## Features

- **Multiple Device Types**:
  - Disk devices with mechanical constraints (seek time, rotational latency)
  - Network devices with bandwidth and latency modeling
  - Printer devices with pages-per-minute throughput

- **Scheduling Algorithms**:
  - FCFS (First-Come-First-Served) - simple queue-based scheduling
  - Priority - scheduling based on request priority levels
  - SCAN (Elevator) - disk scheduling algorithm that minimizes head movement
  - SSTF (Shortest Seek Time First) - optimizes for minimal seek times

- **Workload Generation**:
  - Configurable random request generation with statistical distributions
  - Customizable request parameters (size, priority, location)

- **Performance Metrics**:
  - Device utilization percentages
  - Average request waiting time
  - Average request turnaround time
  - Queue length statistics

## Architecture

The simulation is built around an event-driven architecture with the following key components:

- **Core**: Manages the simulation clock and event queue
- **Devices**: Models different I/O device types with realistic constraints
- **Schedulers**: Implements various scheduling algorithms for request management
- **Request Management**: Generates and tracks I/O requests
- **Statistics Collection**: Gathers performance metrics throughout the simulation

## Running the Simulation

### Prerequisites
- Java 21 or higher
- Gradle (the wrapper is included in the project)

### Build and Run
```bash
# Clone the repository
git clone https://github.com/casca96/io-simulator.git
cd io-simulator

# Build with Gradle
./gradlew build

# Run the simulation
./gradlew run
```

## Available Simulations

The project includes two pre-configured simulations:

### 1. Disk Simulation
Focuses on a single disk device to compare different disk scheduling algorithms (FCFS, SSTF, SCAN).

### 2. Multi-Device Simulation
Models a system with multiple device types (disk, network, printer) each with appropriate scheduling algorithms.

## Customizing Simulations

You can create custom simulations by modifying the `IOSimulation.java` file:

1. Create and configure device instances with appropriate parameters
2. Set up schedulers for each device
3. Configure request generators with desired parameters
4. Register devices with the device manager
5. Schedule initial events and run the simulation

## Interpreting Results

After each simulation run, a summary of statistics is printed including:
- Total number of requests processed
- Average waiting time (time spent in queue)
- Average turnaround time (total time from arrival to completion)
- Device utilization percentage

These metrics help evaluate the effectiveness of different scheduling algorithms for specific workloads.

## Extending the Simulation

The modular design makes it easy to extend the simulation:

- Add new device types by implementing the `Device` interface
- Create new scheduling algorithms by implementing the `Scheduler` interface
- Implement custom request generators for specific workload patterns
- Add new statistics collection metrics in `StatisticsCollector`