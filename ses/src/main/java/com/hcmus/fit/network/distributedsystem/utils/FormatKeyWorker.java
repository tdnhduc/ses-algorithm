package com.hcmus.fit.network.distributedsystem.utils;

import java.net.InetAddress;

public class FormatKeyWorker {
    public static String formatKey(String host, int port){
        return host + ":" + port;
    }
}
