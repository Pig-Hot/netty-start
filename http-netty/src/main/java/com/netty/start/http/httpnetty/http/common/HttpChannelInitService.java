package com.netty.start.http.httpnetty.http.common;

import com.netty.start.http.httpnetty.http.handler.HttpServer2Handler;
import com.netty.start.http.httpnetty.http.handler.HttpServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpChannelInitService extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel sc) {
        sc.pipeline().addLast(new HttpResponseEncoder());
        
        sc.pipeline().addLast(new HttpRequestDecoder());

        sc.pipeline().addLast(new HttpServer2Handler());
    }
    
}