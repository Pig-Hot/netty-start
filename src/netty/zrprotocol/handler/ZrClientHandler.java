package netty.zrprotocol.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.zrprotocol.message.ZrMessage;

/**
 * Created by zhuran on 2018/12/12 0012
 */
public class ZrClientHandler extends SimpleChannelInboundHandler<ZrMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ZrMessage zrMessage) throws Exception {
        System.out.println(zrMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
