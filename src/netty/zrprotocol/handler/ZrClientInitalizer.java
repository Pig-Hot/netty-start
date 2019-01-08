package netty.zrprotocol.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import netty.zrprotocol.coder.ZrDecoder;
import netty.zrprotocol.coder.ZrEncoder;

/**
 * Created by zhuran on 2018/12/12 0012
 */
public class ZrClientInitalizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast("encoder",new ZrEncoder());
        cp.addLast("decoder",new ZrDecoder());
        cp.addLast("handler",new ZrClientHandler());
    }
}
