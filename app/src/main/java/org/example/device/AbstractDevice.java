package org.example.device;

import org.example.request.*;
import org.example.statistics.*;

public abstract class AbstractDevice implements Device {
    protected final String name;
    protected final StatisticsCollector stats;
    protected Request currentRequest;
    protected double busyUntil;
    
    public AbstractDevice(String name, StatisticsCollector stats) {
        this.name = name;
        this.stats = stats;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public boolean isIdle() {
        return currentRequest == null;
    }

    public void completeCurrentRequest() {
        this.currentRequest = null;
    }
}