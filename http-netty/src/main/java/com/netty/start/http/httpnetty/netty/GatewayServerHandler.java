package com.netty.start.http.httpnetty.netty;

import com.netty.start.http.httpnetty.common.GatewayAttribute;
import com.netty.start.http.httpnetty.common.HttpClientUtils;
import com.netty.start.http.httpnetty.common.RouteAttribute;
import com.netty.start.http.httpnetty.common.RoutingLoader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.Signal;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.util.StringUtils;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.netty.start.http.httpnetty.common.GatewayOptions.*;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class GatewayServerHandler extends SimpleChannelInboundHandler<Object> {
    private HttpRequest request;
    private StringBuilder buffer = new StringBuilder();
    private String url = "";
    private String uri = "";
    private StringBuilder respone;
    private GlobalEventExecutor executor = GlobalEventExecutor.INSTANCE;
    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest request = this.request = (HttpRequest) msg;

            //收到客户端的100-Continue协议请求，说明客户端要post数据给服务器
            if (HttpUtil.is100ContinueExpected(request)) {
                notify100Continue(ctx);
            }

            buffer.setLength(0);
            uri = request.uri().substring(1);
        }

        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            if (content.isReadable()) {
                buffer.append(content.toString(GATEWAY_OPTION_CHARSET));
            }

            //获取post数据完毕
            if (msg instanceof LastHttpContent) {
                LastHttpContent trace = (LastHttpContent) msg;

                System.out.println("[NETTY-GATEWAY] REQUEST : " + buffer.toString());

                //根据netty-gateway.properties、netty-route.properties匹配出最终转发的URL地址
                url = matchUrl();
                System.out.println("[NETTY-GATEWAY] URL : " + url);

                //http请求异步转发处理，不要阻塞当前的Netty Handler的I/O线程，提高服务器的吞吐量。
                Future<StringBuilder> future = executor.submit(() -> HttpClientUtils.post(url, buffer.toString()));

                future.addListener((FutureListener<StringBuilder>) future1 -> {
                    if (future1.isSuccess()) {
                        respone = future1.get(GATEWAY_OPTION_HTTP_POST, TimeUnit.MILLISECONDS);
                    } else {
                        respone = new StringBuilder(((Signal) future1.cause()).name());
                    }
                    latch.countDown();
                });

                try {
                    latch.await();
                    writeResponse(respone, future.isSuccess() ? trace : null, ctx);
                    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    //根据netty-gateway.properties、netty-route.properties匹配出最终转发的URL地址
    private String matchUrl() {
        for (GatewayAttribute gateway : RoutingLoader.GATEWAYS) {
            if (gateway.getServerPath().equals(uri)) {
                for (RouteAttribute route : RoutingLoader.ROUTERS) {
                    if (route.getServerPath().equals(uri)) {
                        String[] keys = StringUtils.delimitedListToStringArray(route.getKeyWord(), GATEWAY_OPTION_KEY_WORD_SPLIT);
                        boolean match = true;
                        for (String key : keys) {
                            if (key.isEmpty()) continue;
                            if (buffer.toString().indexOf(key.trim()) == -1) {
                                match = false;
                                break;
                            }
                        }
                        if (match) {
                            return route.getMatchAddr();
                        }
                    }
                }

                return gateway.getDefaultAddr();
            }
        }
        return GATEWAY_OPTION_LOCALHOST;
    }

    //把路由转发的结果应答给http客户端
    private void writeResponse(StringBuilder respone, HttpObject current, ChannelHandlerContext ctx) {
        if (respone != null) {
            boolean keepAlive = HttpUtil.isKeepAlive(request);

            FullHttpResponse response = new DefaultFullHttpResponse(
                    HTTP_1_1, current == null ? OK : current.decoderResult().isSuccess() ? OK : BAD_REQUEST,
                    Unpooled.copiedBuffer(respone.toString(), GATEWAY_OPTION_CHARSET));

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=GBK");

            if (keepAlive) {
                response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }

            ctx.write(response);
        }
    }

    private static void notify100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }
}