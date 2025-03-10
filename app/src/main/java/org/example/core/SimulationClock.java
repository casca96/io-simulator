package org.example.core;

import java.util.*;

/**
 * Event-driven simulation engine that manages time and event execution
 */
public class SimulationClock {
    private double currentTime = 0;
    private PriorityQueue<Event> eventQueue = new PriorityQueue<>(
            Comparator.comparingDouble(Event::getScheduledTime));

    public double getCurrentTime() {
        return currentTime;
    }

    public void scheduleEvent(Event event) {
        eventQueue.add(event);
    }

    public void run(double endTime) {
        while (!eventQueue.isEmpty() && currentTime < endTime) {
            Event nextEvent = eventQueue.poll();
            currentTime = nextEvent.getScheduledTime();
            nextEvent.execute();
        }
    }
}