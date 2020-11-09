package com.hcmus.fit.network.distributedsystem;

import com.hcmus.fit.network.distributedsystem.message.Message;
import com.hcmus.fit.network.distributedsystem.connection.SocketHandler;
import com.hcmus.fit.network.distributedsystem.timemanage.TimeStamp;
import com.hcmus.fit.network.distributedsystem.timemanage.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
    private ObjectOutputStream oos;
    private int currConnections = 0;
    private String processID;
    private Message message;

    public PublishMessage(String processID, List<String> processes, int minConnectionReadyToWrite){
        this.minConnectionReadyToWrite = minConnectionReadyToWrite;
        this.processID = processID;
        this.moveToRawEndPoints(processes);
    }
    private void moveToRawEndPoints(List<String> processes){
        rawEndpoints.clear();
        for(String process : processes){
            List<String> info = Arrays.asList(process.split(":"));
            rawEndpoints.putIfAbsent(info.get(0), info.get(1) + ":" + info.get(2));
            LOGGER.info("Have {} raw end points", rawEndpoints.size());
        }
    }
    private void retryConnectMultiServers(){
        this.currConnections = 0;
        for(Map.Entry<String, String> entry : rawEndpoints.entrySet()){
            String address = entry.getValue();
            String host = address.split(":")[0];
            int port = Integer.valueOf(address.split(":")[1]);
            SocketHandler handler = new SocketHandler(host, port);
            if(handler.getSocket() != null) {
                socketHandlerMap.put(entry.getKey(), handler);
                this.currConnections++;
            }
        }
        this.message = new Message(this.currConnections, processID);
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
                            try {
                                ObjectOutputStream outputStream = entry.getValue().getObjectOutputStream();
                                sendMessage(outputStream, message);
                                LOGGER.info("[SEND][{}] Increase local timestamp, message with timestamp: {}", processID, this.message.getTimeStamp().getTimestampProcess());
                                Thread.sleep(3500L);
                            } catch (IOException e){
                                currConnections--;
                                socketHandlerMap.remove(entry);
                                LOGGER.warn("Client {}:{} is disconnected :cry::cry:", entry.getValue().getHostName(), entry.getValue().getHostPort());
                                if(currConnections < minConnectionReadyToWrite){
                                    LOGGER.error("Oops!!!, there is enough connection to write, waiting...");
                                }
                            }
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void sendMessage(ObjectOutputStream oos, Message msg) throws IOException {
        if(msg.getBuffers() == null){
            msg.setBuffers(Arrays.asList(msg.getTimeStamp().getTimeStamp()));
        }
        msg.getTimeStamp().increaseTimeStamp();
        oos.writeObject(msg);
        oos.reset();
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
