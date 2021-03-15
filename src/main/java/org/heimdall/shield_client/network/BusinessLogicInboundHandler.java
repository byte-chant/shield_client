package org.heimdall.shield_client.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.heimdall.shield_client.message.InboundMessage;
import org.heimdall.shield_client.message.MessageDispatcher;

public class BusinessLogicInboundHandler extends SimpleChannelInboundHandler<InboundMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InboundMessage inboundMessage) throws Exception {
        MessageDispatcher.getInstance().dispatchMessage(ctx.channel(), inboundMessage);
    }

}
