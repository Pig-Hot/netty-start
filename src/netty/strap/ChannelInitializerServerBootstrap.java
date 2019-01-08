package netty.strap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;

import java.net.InetSocketAddress;

/**
 * Created by zhuran on 2018/12/10 0010
 */
class MyChannelInitializer extends ChannelInitializer<Channel> {

    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast(new HttpClientCodec());
    }
}
public class ChannelInitializerServerBootstrap {
    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(),new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new MyChannelInitializer());
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8090));
        try {
            future.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
