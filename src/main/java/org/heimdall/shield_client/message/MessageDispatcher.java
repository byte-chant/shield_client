package org.heimdall.shield_client.message;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageDispatcher {

    private static Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);

    private static volatile MessageDispatcher instance;

    private MessageDispatcher() {

    }

    public static MessageDispatcher getInstance() {
        if(instance == null) {
            synchronized (MessageDispatcher.class) {
                if(instance == null) {
                    instance = new MessageDispatcher();
                }
            }
        }
        return instance;
    }

    public void dispatchMessage(Channel channel, Object message) throws Exception {
        OutboundMessage outboundMessage = (OutboundMessage) message;
        if(outboundMessage.getType().equals(1)){
            PingPongExecutor pingPongExecutor = PingPongExecutor.class.newInstance();
            pingPongExecutor.exec(outboundMessage.getBody());
        }
    }

}
