package com.hcmus.fit.network.distributedsystem.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketHandler {
    private Logger LOGGER = LoggerFactory.getLogger(SocketHandler.class);
    private Socket socket = null;
    private ObjectOutputStream objectOutputStream;

    public SocketHandler(Socket socket){
        this.socket = socket;
    }
    public SocketHandler(String host, int port){
        try{
            this.socket = new Socket(host, port);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
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

    public InputStreamReader getInputStreamReader() throws IOException {
        return new InputStreamReader(this.socket.getInputStream());
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }
}
