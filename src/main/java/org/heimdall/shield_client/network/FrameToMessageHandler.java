package org.heimdall.shield_client.network;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCounted;
import org.heimdall.shield_client.message.InboundMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrameToMessageHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(FrameToMessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object frame) throws Exception {
        byte[] bytes = new byte[((ByteBuf)frame).readableBytes()];
        ((ByteBuf)frame).readBytes(bytes);
        if(isValidFrame(bytes)){
            InboundMessage inboundMessage = new InboundMessage(bytes);
            channelHandlerContext.fireChannelRead(inboundMessage);
        }else{
            logger.error("报文格式不合法，直接丢弃报文。报文长度{},魔数{}", bytes.length, (short)(((bytes[4] & 0xFF) << 8) | (bytes[5] & 0xFF)));
        }
        if(frame instanceof ReferenceCounted){
            //检查ByteBuf的引用计数是否正常，避免内存泄漏
            //ByteBuf的引用计数比较奇葩：刚创建出来的对象引用计数为2；调用retain引用计数增加 increment * 2；调用release引用计数减少 decrement * 2
            //实际的引用计数计算公式：realCnt ：当原始引用计数为奇数，realCnt为0；当原始引用计数为偶数，realCnt = 原始引用计数 / 2；
            //当decrement == realCnt时，意味着此时最后一个指向ByteBuf的引用被释放，此时置原始引用计数为1，也即realCnt为0，对象进入deallocate流程
            //以上逻辑可参考：AbstractReferenceCountedByteBuf.java这个类,其refCnt字段即为原始引用计数
            Integer referCount = ((ByteBuf)frame).refCnt();
            //SimpleChannelInboundHandler稍后会自动释放一次引用,故此处值为1是正常的.
            logger.info("当前报文是ByteBuf类型，当前引用计数{}，状态为{}", referCount, referCount == 1 ? "正常" : "异常");
        }
    }

    private boolean isValidFrame(byte[] bytes){
        if(bytes.length < 40){
            //协议格式：长度，4字节 | 魔数，2字节 | 业务码，2字节 | 唯一标识符，32字节 | 报文体，0 ~ (3M - 40) 字节
            return false;
        }
        if(bytes[4] != 0x25 || bytes[5] != 0x37){
            //拼接起来，对应十进制魔数：9527
            return false;
        }
        return true;
    }
}