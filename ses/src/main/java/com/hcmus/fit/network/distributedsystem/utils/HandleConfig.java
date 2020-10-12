package com.hcmus.fit.network.distributedsystem.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HandleConfig {
    private static HandleConfig instance;
    private static Logger LOGGER = LoggerFactory.getLogger(HandleConfig.class);
    private Properties properties;
    private HandleConfig(){
        properties = loadConfig("application.properties");
    }
    protected Properties loadConfig(String fileName){
        InputStream inputStream = null;
        Properties properties = new Properties();
        try{
            inputStream = HandleConfig.class.getClassLoader().getResourceAsStream(fileName);
            if(inputStream == null){
                LOGGER.error("Cannot load config from {}", fileName);
                return null;
            }
            properties.load(inputStream);
            LOGGER.info("Get from config file");
            properties.entrySet().forEach(e -> LOGGER.info(e.getKey() + " : " + e.getValue()));
            return properties;
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }
    static{
        try{
            instance = new HandleConfig();
        } catch (Exception e){
            throw new RuntimeException("Exception when create handle config");
        }
    }

    public static HandleConfig getInstance() {
        return instance;
    }
    public Properties getProperties(){
        return this.properties;
    }
}
