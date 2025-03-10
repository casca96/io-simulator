package org.example.core;

/**
 * Base abstract class for simulation events
 */
public abstract class AbstractEvent implements Event {
    protected final double scheduledTime;
    protected final SimulationClock clock;
    
    public AbstractEvent(double scheduledTime, SimulationClock clock) {
        this.scheduledTime = scheduledTime;
        this.clock = clock;
    }
    
    @Override
    public double getScheduledTime() {
        return scheduledTime;
    }
}