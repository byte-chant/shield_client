package org.heimdall.shield_client.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //对于出站消息，按照ChannelOutboundHandler添加顺序的逆序执行
        //对于入站消息，按照ChannelInboundHandler添加顺序的正序执行
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast(new BusinessLogicOutboundHandler());
        channelPipeline.addLast("decoder", new LengthFieldBasedFrameDecoder(3 * 1024 * 1024, 0, 4));
        channelPipeline.addLast(new FrameToMessageHandler());
        channelPipeline.addLast(new BusinessLogicInboundHandler());
    }
}
