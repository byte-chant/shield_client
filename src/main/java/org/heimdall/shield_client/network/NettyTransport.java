package org.heimdall.shield_client.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.heimdall.shield_client.config.ConfigManager;
import org.heimdall.shield_client.message.BeanToBytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NettyTransport implements Transport {

    private static final Logger logger = LoggerFactory.getLogger(NettyTransport.class);

    private Channel channel;

    private EventLoopGroup eventLoopGroup;

    public NettyTransport(){
        eventLoopGroup = new NioEventLoopGroup();
    }

    public void start() throws Exception {
        String host = ConfigManager.getInstance().getConfig("server.host");
        Integer port;
        try{
            port = Integer.valueOf(ConfigManager.getInstance().getConfig("server.port"));
        }catch (Exception exception){
            port = 9527;
        }
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new ClientChannelInitializer());
        //异步地连接服务器，调用sync方法阻塞等待直到连接完成
        ChannelFuture channelFuture = bootstrap.connect().sync();
        //我们在Application中使用emptyLoop阻塞住了主线程，直到收到kill信号开始退出程序。
        //故此处不必像《Netty实战》中那样，调用channelFuture.channel().closeFuture().sync()来阻塞主线程
        //通过closeFuture().sync()来阻塞主线程，会在调用channel.close()的时候，被唤醒。
        //相关链接：https://segmentfault.com/q/1010000009070241、https://www.cnblogs.com/heroinss/p/9990445.html、https://www.cnblogs.com/crazymakercircle/p/9902400.html
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                //套接字刚建立的时候，发送一个ping，等待服务器返回一个pong
                future.channel().writeAndFlush(new BeanToBytes((short)1, "ping"));
            }
        });
        channel = channelFuture.channel();
    }

    public void stop() {
        try {
            if (channel != null) {
                channel.close().sync();
            }
            if (eventLoopGroup != null) {
                eventLoopGroup.shutdownGracefully().sync();
            }
        }catch (Exception exception){
            logger.error("Shield Client关闭网络出错，错误信息：" + exception.getMessage());
        }
    }
}
