package org.heimdall.shield_server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class EmptyLoop {

    private static final Logger logger = LoggerFactory.getLogger(EmptyLoop.class);

    private volatile boolean exitFlag;

    private ReentrantLock lock;

    private Condition condition;

    public EmptyLoop(){
        exitFlag = false;
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public void startLoop(){
        lock.lock();
        try {
            while (!exitFlag) {
                logger.info("初始化完毕，主线程进入await");
                condition.await();
                logger.info("主线程从await醒来，开始进入退出流程");
            }
        }catch (InterruptedException exception){
            exception.printStackTrace();
        }
        lock.unlock();
    }

    public void stopLoop(){
        lock.lock();
        try {
            exitFlag = true;
            condition.signalAll();
        }catch (IllegalMonitorStateException exception){
            exception.printStackTrace();
        }
        lock.unlock();
    }
}
