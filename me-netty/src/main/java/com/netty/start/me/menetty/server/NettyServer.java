package com.netty.start.me.menetty.server;

import com.netty.start.me.menetty.NettyConstant;
import com.netty.start.me.menetty.codec.NettyMessageDecoder;
import com.netty.start.me.menetty.codec.NettyMessageEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyServer {

	public void bind() throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>(){
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {					
					ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
					ch.pipeline().addLast(new NettyMessageEncoder());
					ch.pipeline().addLast("readTimeoutHandler",
						new ReadTimeoutHandler(50));
					ch.pipeline().addLast("LoginAuthHandler",
						new LoginAuthRespHandler());
					ch.pipeline().addLast("HeartBeatHandler",
					    new HeartBeatRespHandler());
				}
			});
		// 绑定端口，同步等待成功
		ChannelFuture f = b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
		System.out.println("Netty server start ok : " + (NettyConstant.REMOTEIP + " : " + NettyConstant.PORT));
		// 等待服务端监听端口关闭
		f.channel().closeFuture().sync();
//		//绑定端口
//		b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
//		System.out.println("Netty server start at " + NettyConstant.REMOTEIP + " : " + NettyConstant.PORT);
	}
	
	public static void main(String args[]) throws Exception{
		new NettyServer().bind();
	}
	
	
}
