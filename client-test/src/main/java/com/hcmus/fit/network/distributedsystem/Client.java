package com.hcmus.fit.network.distributedsystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    public static void main(String args[]) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 8080);
        BufferedWriter writer = null;
        int index = 1;
        while(true){
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String message = "Hello my index = " + String.valueOf(index) + "\n";
            writer.write(message);
            index += 1;
            LOGGER.info("Send message to server: {}", message);
            Thread.sleep(1000L);
            writer.flush();
        }
    }
}
