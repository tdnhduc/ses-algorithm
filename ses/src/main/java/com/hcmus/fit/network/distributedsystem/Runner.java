package com.hcmus.fit.network.distributedsystem;

import com.hcmus.fit.network.distributedsystem.config.HandleConfig;
import com.hcmus.fit.network.distributedsystem.message.Message;
import com.hcmus.fit.network.distributedsystem.utils.FormatKeyWorker;
import com.hcmus.fit.network.distributedsystem.connection.SocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Runner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);
    /**
     * workerMap for mapping ip:port with worker
     * eg: 127.0.0.1:8080 <-> worker1
     */
    public static Map<String, HandleMessage>  handleMessageMap = new HashMap<>();

    public static void main(String args[]) throws IOException {
        int port = HandleConfig.getInstance().getHostPort();
        String processID = HandleConfig.getInstance().getProcessID();
        int minConnections = HandleConfig.getInstance().getMinConnections();
        List<String> processes = HandleConfig.getInstance().getListPort();
        Message messageAccept = new Message(minConnections, processID);
        Message messagePending = new Message(minConnections, processID);
        ServerSocket serverSocket;
        serverSocket = new ServerSocket(port);

        if(serverSocket != null){
            LOGGER.info("Server id = {} started at port: {}", processID, port);
            PublishMessage publishMessage = new PublishMessage(processID, processes, minConnections);
            Thread publish = new Thread(publishMessage);
            publish.start();

            while(true){
                SocketHandler socketHandler = new SocketHandler(serverSocket.accept());
                String hostClient = socketHandler.getSocket().getInetAddress().getHostAddress();
                int portClient = socketHandler.getSocket().getPort();

                LOGGER.info("Client {}:{} connected, :grinning::grinning::grinning:", hostClient, portClient);

                HandleMessage handleMessage = new HandleMessage(socketHandler, messageAccept, messagePending);

                String key = FormatKeyWorker.formatKey(handleMessage.getHostName(), handleMessage.getHostPort());

                handleMessageMap.putIfAbsent(key, handleMessage);

                Thread handle = new Thread(handleMessage);

                handle.start();
            }
        } else{
            LOGGER.error("Server is not started, oops crash");
        }
    }
}
