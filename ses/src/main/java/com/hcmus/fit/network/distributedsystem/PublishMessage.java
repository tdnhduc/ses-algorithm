package com.hcmus.fit.network.distributedsystem;

import com.hcmus.fit.network.distributedsystem.utils.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * This class for communicate with each client
 */
public class PublishMessage implements Runnable{
    private static final Logger LOGGER = LoggerFactory.getLogger(PublishMessage.class);
    protected Socket clientSocket = null;
    protected  String processID = null;
    public PublishMessage(Socket clientSocket, String processID){
        this.clientSocket = clientSocket;
        this.processID = processID;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();
            Message message = new Message("HTTP/1.1 200 OK\n\nWorkerRunnable: " +
                    this.processID + " - " +
                    time +
                    "");
            outputStream.write(message.getText().getBytes());
            outputStream.close();
            inputStream.close();
            LOGGER.info("Request processed: " + time);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public InetAddress getIP(){
        return this.clientSocket.getLocalAddress();
    }

    public int getPort() {
        return this.clientSocket.getPort();
    }
}
