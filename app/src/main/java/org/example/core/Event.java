package org.example.core;

/**
 * Interface for events in the discrete event simulation
 */
public interface Event extends Comparable<Event> {
    double getScheduledTime();
    void execute();
    
    @Override
    default int compareTo(Event other) {
        return Double.compare(this.getScheduledTime(), other.getScheduledTime());
    }
}