package netty.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by zhuran on 2018/11/29 0029
 */
@ChannelHandler.Sharable //标记ChannelHandler可以被多个Channel共享
public class EchoServerHandler extends ChannelHandlerAdapter {

    /*在读取操作期间,有异常抛出时会调用*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /*对于每个传入的信息都会调用*/
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        ReferenceCountUtil.release(msg);
        ctx.write(in);
    }

    /*通知ChannelInboundHandler最后一次对channelRead()的调用是当前批量读取中的最后一条消息*/
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }


}
