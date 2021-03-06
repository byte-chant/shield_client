package org.heimdall.shield_client;

import org.heimdall.shield_client.config.ConfigManager;
import org.heimdall.shield_client.network.NetworkBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShieldClient {

    private static final Logger logger = LoggerFactory.getLogger(ShieldClient.class);

    public void start(){
        initConfig();
        initNetwork();
    }

    private void initConfig(){
        ConfigManager.getInstance().loadConfig();
        logger.info("初始化配置成功");
    }

    private void initNetwork(){
        try {
            NetworkBuilder.getInstance().start();
            logger.info("初始化网络成功");
        } catch (Exception exception){
            logger.info("初始化网络失败，程序退出。" + exception.getMessage());
            stop();
            System.exit(-1);
        }
    }

    public void stop(){
        unInitConfig();
        unInitNetwork();
    }

    public void unInitConfig(){
        ConfigManager.getInstance().unloadConfig();
        logger.info("反初始化配置成功");
    }

    public void unInitNetwork(){
        NetworkBuilder.getInstance().stop();
        logger.info("反初始化网络成功");
    }

}
