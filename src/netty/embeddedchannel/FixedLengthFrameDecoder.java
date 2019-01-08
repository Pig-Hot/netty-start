package netty.embeddedchannel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by zhuran on 2018/12/10 0010
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength) {
        if (frameLength <= 0) {
            try {
                throw new IllegalAccessException("frameLength must be a positive integer: " + frameLength);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        this.frameLength = frameLength;
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
        while (in.readableBytes() >= frameLength) {
            ByteBuf buf = in.readBytes(frameLength);
            out.add(buf);
        }
    }
}
