package com.hcmus.fit.network.distributedsystem;

import com.hcmus.fit.network.distributedsystem.utils.SocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HandleMessage implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(HandleMessage.class);
    public SocketHandler socketHandler;
    private String message = null;
    public HandleMessage(SocketHandler socketHandler){
        this.socketHandler = socketHandler;
    }
    @Override
    public void run() {
        try {
            InputStream inputStream = this.socketHandler.getSocket().getInputStream();
            BufferedReader buffer = null;
            while(socketHandler.isConnected()){
                buffer = new BufferedReader(new InputStreamReader(inputStream));
                if(buffer.ready()){
                    message = buffer.readLine();
                    LOGGER.info("Received : {}", message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return message;
    }
}
