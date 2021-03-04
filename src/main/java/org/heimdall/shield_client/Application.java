package org.heimdall.shield_client;

import org.heimdall.shield_client.config.EmptyLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static EmptyLoop emptyLoop = new EmptyLoop();

    private static class ApplicationHandler implements SignalHandler{
        public void handle(Signal signal) {
            logger.info("收到kill信号，即将退出 Shield Client");
            if(emptyLoop != null){
                emptyLoop.stopLoop();
            }
        }
    }

    public static void main(String[] args) {
        ShieldClient shieldClient = new ShieldClient();
        shieldClient.start();
        ApplicationHandler applicationHandler = new ApplicationHandler();
        Signal.handle(new Signal("TERM"), applicationHandler);
        emptyLoop.startLoop();
        shieldClient.stop();
        logger.info("Shield Client 成功关闭，欢迎再次使用！");
    }
}
