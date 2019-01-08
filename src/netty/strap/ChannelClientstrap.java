package netty.strap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by zhuran on 2018/12/10 0010
 */
public class ChannelClientstrap {
    public static void main(String[] args) {
        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        Bootstrap bootstrap = new Bootstrap();
                        bootstrap.channel(NioSocketChannel.class)
                                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

                                    }
                                });
                        //使用与分配给已被接受的子Channel相同的EventLoop
                        bootstrap.group(ctx.channel().eventLoop());
                        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("www.manning.com", 80)).sync();
                    }

                });
        ChannelFuture future = null;
        try {
            future = bootstrap.bind(new InetSocketAddress(9999)).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert future != null;
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("success");
                } else {
                    System.out.println("fail");
                    future.cause().printStackTrace();
                }
            }
        });
    }
}
