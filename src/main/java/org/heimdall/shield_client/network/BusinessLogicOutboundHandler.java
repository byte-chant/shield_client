package org.heimdall.shield_client.network;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.heimdall.shield_client.message.OutboundMessage;

public class BusinessLogicOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //出站消息按照ChannelInitializer添加的逆序依次执行
        //只有在最后加上这行super.write才能继续将出站消息抛给ChannelPipeLine下一个Handler
        super.write(ctx, Unpooled.copiedBuffer(((OutboundMessage)msg).toByteArray()), promise);
    }
}