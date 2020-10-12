package com.hcmus.fit.network.distributedsystem.utils;

import java.net.InetAddress;

public class FormatKeyWorker {
    public static String formatKey(InetAddress address, int port){
        return address.getHostAddress() + ":" + port;
    }
}
