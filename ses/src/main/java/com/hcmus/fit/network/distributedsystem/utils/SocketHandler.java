package com.hcmus.fit.network.distributedsystem.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketHandler {
    private Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);
    private Socket socket = null;
    public SocketHandler(Socket socket){
        this.socket = socket;
    }
    public SocketHandler(String host, int port){
        try{
            this.socket = new Socket(host, port);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isConnected(){
        return this.socket.isConnected();
    }

    public String getHostName(){
        return this.socket.getInetAddress().getHostName();
    }

    public int getHostPort(){
        return this.socket.getPort();
    }
}
