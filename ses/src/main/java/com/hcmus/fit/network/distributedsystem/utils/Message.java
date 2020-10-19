package com.hcmus.fit.network.distributedsystem.utils;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
    private String message;
    private List<String> buffers;
    private int[] timeStamp;

    public Message(){
        this.message = "This is a init message";
    }
    public Message(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
