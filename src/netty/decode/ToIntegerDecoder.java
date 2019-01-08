package netty.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by zhuran on 2018/12/10 0010
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //检查是否有4个字节可读
        if(in.readableBytes() >= 4){
            //从入站的ByteBuf中读取一个int
            out.add(in.readInt());
        }
    }
}
