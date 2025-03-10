package org.example.request;

/**
 * Interface for components that generate I/O requests
 */
public interface RequestGenerator {
    Request generateRequest(double currentTime);
}