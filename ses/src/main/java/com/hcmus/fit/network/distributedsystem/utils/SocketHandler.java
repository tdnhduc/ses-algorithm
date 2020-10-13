package com.hcmus.fit.network.distributedsystem.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.net.SocketAddress;

public class SocketHandler {
    private Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);
    private final Socket socket;
    public SocketHandler(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isConnected(){
        return this.socket.isConnected();
    }
}
