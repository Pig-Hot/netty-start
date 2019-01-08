package netty.zrprotocol.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import netty.zrprotocol.message.ZrHeader;
import netty.zrprotocol.message.ZrMessage;

/**
 * Created by zhuran on 2018/12/12 0012
 */
public class ZrEncoder extends MessageToByteEncoder<ZrMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ZrMessage zrMessage, ByteBuf byteBuf) throws Exception {
        //将消息转换为二进制
        ZrHeader header = zrMessage.getHeader();
        //写入header
        byteBuf.writeInt(header.getVersion());
        byteBuf.writeInt(header.getContentLength());
        byteBuf.writeBytes(header.getSessionId().getBytes());
        //写入消息主体
        byteBuf.writeBytes(zrMessage.getBody().getBytes());
    }
}
