package org.heimdall.shield_client.network;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.heimdall.shield_client.message.MessageDispatcher;
import org.heimdall.shield_client.message.OutboundMessage;

public class BusinessLogicOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object outboundMessage, ChannelPromise promise) throws Exception {
        if (outboundMessage instanceof OutboundMessage) {
            MessageDispatcher.getInstance().dispatchMessage(ctx.channel(), outboundMessage);
            //只有在最后加上这行super.write才能继续将出站消息抛给ChannelPipeLine下一个Handler
            super.write(ctx, Unpooled.copiedBuffer(((OutboundMessage)outboundMessage).toByteArray()), promise);
        } else {
            super.write(ctx, outboundMessage, promise);
        }
    }
}