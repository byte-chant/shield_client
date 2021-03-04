package org.heimdall.shield_server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.heimdall.shield_server.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NettyTransport implements Transport {

    private static final Logger logger = LoggerFactory.getLogger(NettyTransport.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workGroup;

    private ChannelFuture channelFuture;

    public NettyTransport(){
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
    }

    public void start() throws Exception {
        Integer port;
        try{
            port = Integer.valueOf(ConfigManager.getInstance().getConfig("server.port"));
        }catch (Exception exception){
            port = 9527;
        }
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ServerChannelInitializer());
        //异步地绑定服务器，调用sync方法阻塞等待直到绑定完成
        channelFuture = bootstrap.bind().sync();
        //我们在Application中使用emptyLoop阻塞住了主线程，直到收到kill信号开始退出程序。
        //故此处不必像《Netty实战》中那样，调用channelFuture.channel().closeFuture().sync()来阻塞主线程
        //通过closeFuture().sync()来阻塞主线程，会在调用channel.close()的时候，被唤醒。
        //相关链接：https://segmentfault.com/q/1010000009070241、https://www.cnblogs.com/heroinss/p/9990445.html、https://www.cnblogs.com/crazymakercircle/p/9902400.html
    }

    public void stop() {
        try {
            if (bossGroup != null) {
                bossGroup.shutdownGracefully().sync();
            }
            if (workGroup != null) {
                workGroup.shutdownGracefully().sync();
            }
        }catch (Exception exception){
            logger.error("Shield Server关闭网络出错，错误信息：" + exception.getMessage());
        }
    }
}
