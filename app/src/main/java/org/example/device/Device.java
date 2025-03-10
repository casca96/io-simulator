package org.example.device;

import org.example.request.*;

/**
 * Interface for I/O devices that process requests
 */
public interface Device {
    void processRequest(Request request, double currentTime);
    boolean isIdle();
    String getName();
    double getEstimatedCompletionTime(Request request, double currentTime);
}