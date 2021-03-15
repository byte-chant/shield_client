package org.heimdall.shield_client.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.heimdall.shield_client.message.InboundMessage;

public class BusinessLogicInboundHandler extends SimpleChannelInboundHandler<InboundMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InboundMessage inboundMessage) throws Exception {

    }

}
