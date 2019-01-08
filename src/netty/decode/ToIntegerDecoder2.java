package netty.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * Created by zhuran on 2018/12/10 0010
 */
public class ToIntegerDecoder2 extends ReplayingDecoder {
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //传入的ByteBuf是ReplayingDecoderByteBuf
        //从入站的ByteBuf中读取一个int
        out.add(in.readInt());
    }
}
