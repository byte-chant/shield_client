package org.heimdall.shield_server.network;

public class NetworkBuilder {

    private static volatile Transport instance;

    public static Transport getInstance(){
        if(instance == null){
            synchronized (NetworkBuilder.class){
                if(instance == null){
                    instance = new NettyTransport();
                }
            }
        }
        return instance;
    }

}
