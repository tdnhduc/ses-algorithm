package com.hcmus.fit.network.distributedsystem.utils;

public class Tuple {
    private String pid;
    int[] timeStamp;

    public Tuple(String pid, int[] timeStamp){
        this.pid = pid;
        this.timeStamp = timeStamp;
    }

    public void setTimeStamp(int[] timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int[] getTimeStamp() {
        return timeStamp;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPid() {
        return pid;
    }
}
