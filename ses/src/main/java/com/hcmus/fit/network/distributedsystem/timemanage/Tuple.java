package com.hcmus.fit.network.distributedsystem.timemanage;

import java.io.Serializable;

public class Tuple implements Serializable {
    private int pid;
    private int[] timestamp;

    public Tuple(int numOfProcess, int index){
        this.pid = index;
        this.timestamp = new int[numOfProcess + 1];
    }

    public int getPid() {
        return pid;
    }

    public synchronized void increaseTimeStamp(){
        this.timestamp[pid]++;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int[] getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int[] timestamp) {
        this.timestamp = timestamp;
    }
}
