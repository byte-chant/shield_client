package org.heimdall.shield_client;

import org.heimdall.shield_client.config.ApplicationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static EmptyLoop emptyLoop = new EmptyLoop();

    public static void main(String[] args) {
        ShieldClient shieldClient = new ShieldClient();
        shieldClient.start();
        ApplicationHandler applicationHandler = new ApplicationHandler(emptyLoop);
        applicationHandler.config(new Signal("TERM"));
        emptyLoop.startLoop();
        shieldClient.stop();
        logger.info("Shield Client 成功关闭，欢迎再次使用！");
    }
}
