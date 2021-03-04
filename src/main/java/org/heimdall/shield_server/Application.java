package org.heimdall.shield_server;

import org.heimdall.shield_server.config.EmptyLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static EmptyLoop emptyLoop = new EmptyLoop();

    private static class ApplicationHandler implements SignalHandler{
        public void handle(Signal signal) {
            logger.info("收到kill信号，即将退出 Shield Server");
            if(emptyLoop != null){
                emptyLoop.stopLoop();
            }
        }
    }

    public static void main(String[] args) {
        ShieldServer shieldServer = new ShieldServer();
        shieldServer.start();
        ApplicationHandler applicationHandler = new ApplicationHandler();
        Signal.handle(new Signal("TERM"), applicationHandler);
        emptyLoop.startLoop();
        shieldServer.stop();
        logger.info("Shield Server 成功关闭，欢迎再次使用！");
    }
}
