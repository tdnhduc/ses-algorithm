package com.hcmus.fit.network.distributedsystem;

import com.hcmus.fit.network.distributedsystem.utils.Message;
import com.hcmus.fit.network.distributedsystem.utils.SocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.*;

/**
 * This class for communicate with each client
 */
public class PublishMessage implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(PublishMessage.class);
    private Map<String, String> rawEndpoints = new HashMap<>();
    private Map<String, SocketHandler> socketHandlerMap = new HashMap();
    private int minConnectionReadyToWrite;
    private int currConnections = 0;
    private String processID;
    public PublishMessage(String processID, List<String> processes, int minConnectionReadyToWrite){
        this.minConnectionReadyToWrite = minConnectionReadyToWrite;
        this.processID = processID;
        this.moveToRawEndPoints(processes);
    }
    private void moveToRawEndPoints(List<String> processes){
        for(String process : processes){
            List<String> info = Arrays.asList(process.split(":"));
            rawEndpoints.putIfAbsent(info.get(0), info.get(1) + ":" + info.get(2));

        }
    }
    private void retryConnectMultiServers(){
        this.currConnections = 0;
        for(Map.Entry<String, String> entry : rawEndpoints.entrySet()){
            String address = entry.getValue();
            String host = address.split(":")[0];
            int port = Integer.valueOf(address.split(":")[1]);
            SocketHandler handler = new SocketHandler(host, port);
            if(handler.getSocket() != null){
                socketHandlerMap.put(entry.getKey(), handler);
                this.currConnections ++;
            }
        }
        LOGGER.info("Number server available after retry = {}", currConnections);
    }
    @Override
    public void run() {
        try {
            while(true){
                if(currConnections == 0){
                    LOGGER.warn("No connection available, refresh connect now!!!");
                    retryConnectMultiServers();
                    Thread.sleep(1500L);
                } else{
                    while (currConnections >= minConnectionReadyToWrite){
                        for(Map.Entry<String, SocketHandler> entry : socketHandlerMap.entrySet()){
                            InputStream inputStream = entry.getValue().getSocket().getInputStream();
                            OutputStream outputStream = entry.getValue().getSocket().getOutputStream();
                            long time = System.currentTimeMillis();
                            Message message = new Message("HTTP/1.1 200 OK\n\nProcessId: " +
                                     processID + " - " +
                                    time +
                                    "");
                            outputStream.write(message.getText().getBytes());
                            LOGGER.info("Request processed: " + time);
                            Thread.sleep(1500L);
                        }
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public InetAddress getIP(String processID){
        SocketHandler handler = socketHandlerMap.get(processID);
        return handler.getSocket().getLocalAddress();
    }

    public int getHostPort(String processID) {
        SocketHandler handler = socketHandlerMap.get(processID);
        return handler.getSocket().getPort();
    }
}
