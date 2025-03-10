package org.example.scheduler;

import org.example.device.*;
import org.example.request.*;

/**
 * Interface for request schedulers that implement different queuing algorithms
 */
public interface Scheduler {
    void addRequest(Request request);
    Request getNextRequest(Device device, double currentTime);
    int getQueueLength();
    String getName();
}