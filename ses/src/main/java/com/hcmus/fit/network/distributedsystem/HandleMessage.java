package com.hcmus.fit.network.distributedsystem;

import com.hcmus.fit.network.distributedsystem.message.Message;
import com.hcmus.fit.network.distributedsystem.timemanage.Buffer;
import com.hcmus.fit.network.distributedsystem.timemanage.TimeStamp;
import com.hcmus.fit.network.distributedsystem.connection.SocketHandler;
import com.hcmus.fit.network.distributedsystem.timemanage.Tuple;
import com.hcmus.fit.network.distributedsystem.timemanage.VectorClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class HandleMessage implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(HandleMessage.class);
    public SocketHandler socketHandler;
    private Message messageAccept;
    private Message messagePending;
    private ObjectInputStream oos;
    public HandleMessage(SocketHandler socketHandler, Message messageAccept, Message messagePending) throws IOException {
        this.socketHandler = socketHandler;
        this.messageAccept = messageAccept;
        this.messagePending = messagePending;
        this.oos = new ObjectInputStream(socketHandler.getSocket().getInputStream());
    }
    @Override
    public void run() {
        LOGGER.info("Waiting for your message :cry: :cry:");
        while(true){
            try {
                handleMessage((Message) this.oos.readObject());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                LOGGER.error(e.getMessage());
                break;
            }
        }

    }

    private synchronized void handleMessage(Message message){
        this.messageAccept.getTimeStamp().increaseTimeStamp();
        if(Buffer.deliveryCondition(message.getBuffers(), this.messageAccept.getTimeStamp().getTimeStamp())){
            LOGGER.info("[ACCEPT] message from [{}] have been accept: {}", message.getPid(), VectorClock.toString(message.getTimeStamp()));
            LOGGER.info("[ACCEPT] message from [{}] have buffer", message.getPid());
            for(Tuple pid : message.getBuffers()){
                LOGGER.info("Pid [{}], timestamp {}", pid.getPid(), pid.getTimestamp());
            }
            this.messageAccept.setBuffers(message.getBuffers());
        } else{
            LOGGER.info("Current Vector Clock: {}", VectorClock.toString(this.messageAccept.getTimeStamp()));
            LOGGER.info("[BUFFER] message: {}", VectorClock.toString(message.getTimeStamp()));
            //this.message.add(m);
            this.messagePending.setBuffers(message.getBuffers());
        }
        //LOGGER.info("[RECEIVED] Increase local timestamp, message from [{}] with timestamp:  {}", message.getPid(), message.getTimeStamp().getTimestampProcess());
    }

    public Message getMessageAccept() {
        return messageAccept;
    }

    public Message getMessagePending() {
        return messagePending;
    }

    public String getHostName(){
        return this.socketHandler.getHostName();
    }

    public int getHostPort(){
        return this.socketHandler.getHostPort();
    }
}
