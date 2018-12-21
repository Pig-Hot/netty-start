package com.netty.start.http.httpnetty.boot;

import com.netty.start.http.httpnetty.common.GatewayOptions;
import com.netty.start.http.httpnetty.common.HostInfo;
import com.netty.start.http.httpnetty.common.JvmInfo;
import com.netty.start.http.httpnetty.netty.GatewayServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.io.PrintWriter;

import static com.netty.start.http.httpnetty.common.GatewayOptions.GATEWAY_OPTION_PARALLEL;


public class GatewayServer {

    public static int PORT = 0;
    private static final JvmInfo JVM_INFO = new JvmInfo();
    private static final HostInfo HOST_INFO = new HostInfo();

    private static final void dumpSystemInfo(PrintWriter out) {
        out.println("               __  __                    __                          ");
        out.println("   ____  ___  / /_/ /___  ______ _____ _/ /____ _      ______ ___  __");
        out.println("  / __ \\/ _ \\/ __/ __/ / / / __ `/ __ `/ __/ _ \\ | /| / / __ `/ / / /");
        out.println(" / / / /  __/ /_/ /_/ /_/ / /_/ / /_/ / /_/  __/ |/ |/ / /_/ / /_/ / ");
        out.println("/_/ /_/\\___/\\__/\\__/\\__, /\\__, /\\__,_/\\__/\\___/|__/|__/\\__,_/\\__, /  ");
        out.println("                   /____//____/                             /____/   ");
        out.println();
        out.println("NettyGateway 1.0,Build 2018/4/18,Author:tangjie");
        out.println("https://github.com/tang-jie/NettyGateway");
        out.print(JVM_INFO);
        out.print(HOST_INFO);
        out.println("PORT : " + PORT + "\n");
        out.println("NettyGateway start success!");
        out.flush();
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:netty-gateway.xml");
        PORT = ((GatewayOptions) context.getBean("options")).getGatewayPort();

        dumpSystemInfo(new PrintWriter(System.out));

        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("netty.gateway.boss", Thread.MAX_PRIORITY));
        EventLoopGroup workerGroup = new NioEventLoopGroup(GATEWAY_OPTION_PARALLEL, new DefaultThreadFactory("netty.gateway.worker", Thread.MAX_PRIORITY));

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new GatewayServerInitializer());
            Channel ch = b.bind(PORT).sync().channel();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}