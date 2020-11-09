package com.hcmus.fit.network.distributedsystem.timemanage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

public class TimeStamp implements Serializable {
    private static final long serialVersionUID = 1L;
    private Tuple timestamp;
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeStamp.class);

    public TimeStamp(int numOfProcess, int index){
        this.timestamp = new Tuple(numOfProcess, index);
    }

    public void increaseTimeStamp(){
        this.timestamp.increaseTimeStamp();
    }

    public void setTimestamp(int[] timestamp) {
        this.timestamp.setTimestamp(timestamp);
    }

    public Tuple getTimeStamp(){
        return this.timestamp;
    }

    public int getPid() {
        return this.timestamp.getPid();
    }

    public int[] getTimestampProcess() {
        return this.timestamp.getTimestamp();
    }

}
