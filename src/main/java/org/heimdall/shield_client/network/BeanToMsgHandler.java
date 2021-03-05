package org.heimdall.shield_client.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class BeanToMsgHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //出站消息按照ChannelInitializer添加的逆序依次执行
        //只有在最后加上这行super.write才能继续将出站消息抛给ChannelPipeLine下一个Handler
        super.write(ctx, msg, promise);
    }
}