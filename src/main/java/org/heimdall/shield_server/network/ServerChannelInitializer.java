package org.heimdall.shield_server.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast("decoder", new LengthFieldBasedFrameDecoder(3 * 1024 * 1024, 0, 4));
        channelPipeline.addLast(new MsgToBeanHandler());
    }
}
