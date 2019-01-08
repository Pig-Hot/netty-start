package netty.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by zhuran on 2018/12/11 0011
 */
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {

    protected void encode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
            out.add(String.valueOf(msg));
    }
}
