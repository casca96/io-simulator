package org.example.request;

/**
 * Represents an I/O request with attributes like type, priority, and size
 */
public class Request {
    private static int nextId = 0;
    
    private final int id;
    private final RequestType type;
    private final int priority;
    private final double arrivalTime;
    private final int size;
    private final int location; // For block devices like disks
    
    private double startTime;
    private double completionTime;
    
    public enum RequestType {
        READ, WRITE
    }
    
    public Request(RequestType type, int priority, double arrivalTime, int size, int location) {
        this.id = nextId++;
        this.type = type;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.size = size;
        this.location = location;
    }
    
    // Getters
    public int getId() { return id; }
    public RequestType getType() { return type; }
    public int getPriority() { return priority; }
    public double getArrivalTime() { return arrivalTime; }
    public int getSize() { return size; }
    public int getLocation() { return location; }
    
    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }
    
    public void setCompletionTime(double completionTime) {
        this.completionTime = completionTime;
    }
    
    public double getWaitingTime() {
        return startTime - arrivalTime;
    }
    
    public double getServiceTime() {
        return completionTime - startTime;
    }
    
    public double getTurnaroundTime() {
        return completionTime - arrivalTime;
    }
    
    @Override
    public String toString() {
        return "Request[id=" + id + ", type=" + type + 
               ", priority=" + priority + ", size=" + size + 
               ", location=" + location + "]";
    }
}