package netty.zrprotocol.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.zrprotocol.message.ZrMessage;

/**
 * Created by zhuran on 2018/12/12 0012
 */
public class ZrServerHandler extends SimpleChannelInboundHandler<ZrMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ZrMessage zrMessage) throws Exception {
        System.out.println("[Header: " + zrMessage.getHeader() + " Body: " + zrMessage.getBody() + "]");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "连接...");
    }
}
