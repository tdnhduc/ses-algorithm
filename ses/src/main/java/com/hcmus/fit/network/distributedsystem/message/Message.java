package com.hcmus.fit.network.distributedsystem.message;

import com.hcmus.fit.network.distributedsystem.timemanage.TimeStamp;
import com.hcmus.fit.network.distributedsystem.timemanage.Tuple;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
    private int pid;
    private int minConnectionReadyToWrite;
    private TimeStamp timeStamp;
    private List<Tuple> buffers;

    public Message(String pid){
        this.pid = Integer.valueOf(pid);
        this.timeStamp = new TimeStamp(minConnectionReadyToWrite, this.pid);
    }

    public Message(int minConnectionReadyToWrite, String pid){
        this.pid = Integer.valueOf(pid);
        this.minConnectionReadyToWrite = minConnectionReadyToWrite;
        this.timeStamp = new TimeStamp(minConnectionReadyToWrite, this.pid);
    }

    public void setBuffers(List<Tuple> bufferIncoming) {
        if(this.buffers == null){
            this.buffers = bufferIncoming;
        }else{
            for(Tuple buffer : bufferIncoming){
                boolean flag;
                for(Tuple buffLocal : this.buffers){
                    flag = true;
                    if(buffer.getPid() == buffLocal.getPid()){
                        for(int i = 0; i < buffLocal.getTimestamp().length; i++){
                            buffLocal.getTimestamp()[i] = buffLocal.getTimestamp()[i] >= buffer.getTimestamp()[i] ? buffLocal.getTimestamp()[i] : buffer.getTimestamp()[i];
                        }
                        flag = false;
                        break;
                    }
                    if(flag){
                        this.buffers.add(buffer);
                    }
                }

            }
        }
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setTimeStamp(TimeStamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getPid() {
        return pid;
    }

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    public List<Tuple> getBuffers() {
        return buffers;
    }
}
