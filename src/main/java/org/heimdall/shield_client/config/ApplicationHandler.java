package org.heimdall.shield_client.config;

import org.heimdall.shield_client.EmptyLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class ApplicationHandler implements SignalHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationHandler.class);

    private EmptyLoop emptyLoop;

    public ApplicationHandler(EmptyLoop emptyLoop){
        this.emptyLoop = emptyLoop;
    }

    public void config(Signal signal){
        Signal.handle(signal, this);
    }

    public void handle(Signal signal) {
        logger.info("收到kill信号，即将退出 Shield Client");
        if(emptyLoop != null){
            emptyLoop.stopLoop();
        }
    }

}