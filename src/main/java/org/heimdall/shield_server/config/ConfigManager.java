package org.heimdall.shield_server.config;

import org.heimdall.shield_library.utility.PropertyUtil;

import java.util.Properties;

public class ConfigManager {

    private static volatile ConfigManager instance;

    private Properties properties;

    public static ConfigManager getInstance(){
        if(instance == null){
            synchronized (ConfigManager.class){
                if(instance == null){
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public synchronized void loadConfig(){
        properties = PropertyUtil.loadConfig("d:\\application.properties");
    }

    public synchronized void unloadConfig(){
        PropertyUtil.flushConfig(properties, "d:\\application.properties");
        properties.clear();
        properties = null;
    }

    public synchronized String getConfig(String key){
        return properties.getProperty(key, "");
    }

    public synchronized void setConfig(String key, String val){
        properties.setProperty(key, val);
        PropertyUtil.flushConfig(properties, "d:\\application.properties");
    }

}
