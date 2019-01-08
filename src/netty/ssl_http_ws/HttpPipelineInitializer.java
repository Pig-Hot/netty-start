package netty.ssl_http_ws;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * Created by zhuran on 2018/12/11 0011
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {
    private final boolean client;
    private final SslContext context;

    public HttpPipelineInitializer(SslContext context,boolean client) {
        this.context = context;
        this.client = client;
    }

    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //增加SSL使用HTTPS
        SSLEngine engine = context.newEngine(ch.alloc());
        pipeline.addFirst("ssl", new SslHandler(engine));
        if (client) {
            //http客户端
            //1
            pipeline.addLast("decoder", new HttpResponseDecoder());
            pipeline.addLast("encoder", new HttpRequestEncoder());
            //2
            pipeline.addLast("codec",new HttpClientCodec());
            //处理压缩内容
            pipeline.addLast("decompressor",
                    new HttpContentDecompressor());
        } else {
            //http服务端
            //1
            pipeline.addLast("decoder", new HttpRequestDecoder());
            pipeline.addLast("encoder", new HttpResponseEncoder());
            //2
            pipeline.addLast("codec",new HttpServerCodec());
            //处理压缩内容
            pipeline.addLast("compressor",
                    new HttpContentCompressor());
        }
        //将最大的消息大小为 512 KB 的 HttpObjectAggregator添加到ChannelPipeline
        pipeline.addLast("aggregator",
                new HttpObjectAggregator(512 * 1024));
    }
}
