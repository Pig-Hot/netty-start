package netty.echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by zhuran on 2018/11/29 0029
 * 创建一个ServerBootStrap实例,因为是选择NIO传输,所以指定了NioEventLoopGroup来接受和处理新的连接,并将Channel指定为NioServer
 * - SocketChannel。在此之后，你将本地地址设置为一个具有选定端口的InetSocket-Address。服务器将绑定到这个地址以监听新的连接请求。
 * 使用了一个特殊的类 —— ChannelInitializer当一个新的连接被接受时，一个新的子Channel将会被创建，而 ChannelInitializer将会把一个你的
 * EchoServerHandler 的实例添加到该Channel的ChannelPipeline中。正如我们之前所解释的，这个ChannelHandler将会收到有关入站消息的通知
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() {
        //创建EventLoopGroup
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        //创建BootStrap
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                //指定所使用的NIO传输Channel
                .channel(NioServerSocketChannel.class)
                //指定所使用的套接字端口
                .localAddress(new InetSocketAddress(port))
                //添加一个EchoServerHandler到字Channel的ChannelPipline
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //EchoServerHandler被标记为@Shareable,所以我们总是可以使用同样的实例
                        socketChannel.pipeline().addLast(echoServerHandler);
                    }
                });
        try {
            //异步地绑定服务器,调用sync方法阻塞等待直到完成
            ChannelFuture f = bootstrap.bind().sync();
            //获取Channel的CloseFuture,并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭EventLoopGroup所有资源
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        EchoServer echoServer = new EchoServer(5555);
        echoServer.start();
    }
}
