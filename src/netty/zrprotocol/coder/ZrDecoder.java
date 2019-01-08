package netty.zrprotocol.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import netty.zrprotocol.message.ZrHeader;
import netty.zrprotocol.message.ZrMessage;

import java.util.List;

/**
 * Created by zhuran on 2018/12/12 0012
 */
public class ZrDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int version = byteBuf.readInt();
        int contentLength = byteBuf.readInt();
        byte[] sessionByte = new byte[36];
        byteBuf.readBytes(sessionByte);
        String sessionId = new String(sessionByte);
        //组装Header
        ZrHeader header = new ZrHeader(version,contentLength,sessionId);
        //读取剩下所有字节
        byte[] contentbys = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(contentbys);
        String content = new String(contentbys);
        //组装消息
        ZrMessage message = new ZrMessage(header, content);
        list.add(message);
    }
}
