package com.hcmus.fit.network.distributedsystem;

import com.hcmus.fit.network.distributedsystem.utils.FormatKeyWorker;
import com.hcmus.fit.network.distributedsystem.utils.HandleConfig;
import com.hcmus.fit.network.distributedsystem.utils.SocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Runner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);
    /**
     * workerMap for mapping ip:port with worker
     * eg: 127.0.0.1:8080 <-> worker1
     */
    public static Map<String, PublishMessage> workerMap = new HashMap<>();

    public static void main(String args[]) throws IOException {
        Properties props = HandleConfig.getInstance().getProperties();
        String port = props.getProperty("port");
        String processID = props.getProperty("process_id");
        ServerSocket serverSocket = new ServerSocket(Integer.valueOf(port));
        if(serverSocket != null){
            LOGGER.info("Server started");
            while(true){
                SocketHandler socketHandler = new SocketHandler(serverSocket.accept());
                LOGGER.info("Some client connected, hihihi");
                PublishMessage publishMessage = new PublishMessage(socketHandler, processID);
                HandleMessage handleMessage = new HandleMessage(socketHandler);
                String key = FormatKeyWorker.formatKey(publishMessage.getIP(), publishMessage.getPort());
                workerMap.putIfAbsent(key, publishMessage);
                Thread t = new Thread(handleMessage);
                t.start();
            }
        } else{
            LOGGER.error("Server is not started, oops crash");
        }
    }

    private void publishMessage(){
        for(Map.Entry<String, PublishMessage> entry : workerMap.entrySet()){

        }
    }
}
