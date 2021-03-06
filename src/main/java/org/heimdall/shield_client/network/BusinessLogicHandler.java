package org.heimdall.shield_client.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.heimdall.shield_client.message.BeanToBytes;
import org.heimdall.shield_client.message.BytesToBean;

public class BusinessLogicHandler extends SimpleChannelInboundHandler<BytesToBean> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //套接字刚建立的时候，发送一个ping，等待服务器返回一个pong
        //Unpooled.copiedBuffer返回的是非池化的UnpooledHeapByteBuf，JVM会根据GC自动回收；不需要手动维护引用计数，但性能比池化的ByteBuf要低
        //相关链接：https://zhuanlan.zhihu.com/p/30849776
        ByteBuf byteBuf = Unpooled.copiedBuffer(new BeanToBytes((short)1, "ping").toByteArray());
        ctx.channel().writeAndFlush(byteBuf);
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BytesToBean msg) throws Exception {

    }
}