package netty.wechat;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;

/**
 * Created by zhuran on 2018/12/11 0011
 * ---------------------------------------------------------------------------------------------------------------------
 * 如果HTTP请求地址指向/ws的URI,那么HTTPRequestHandler将会调用FullHttpRequest对象上的retain方法,并通过调用fireChannelRead(msg)
 * 将它转发给下一个ChannelInboundHandler,之所以需要调用retain方法是因为ChanelRead方法完成后会调用release方法来释放资源
 * 客户端发送了一个HTTP信息头为Expect:100-continue,那么HttpRequestHandler将会发送一个100Continue的响应,在该HTTP信息头被设置后Htt
 * -pResponseHandler将会写回一个HttpResponse给客户端
 * 如果不需要加密,最好使用DefaultFileRegion 中来达到最佳效率。这将会利用零拷贝特性来进行内容的传输,否则你可以使用 ChunkedNioFile
 * HttpRequestHandler 将写一个 LastHttpContent 来标记响应的结束。如果没有请求 keep-alive ，那么 HttpRequestHandler 将会添加一个
 * ChannelFutureListener到最后一次写出动作的 ChannelFuture，并关闭该连接。在这里，你将调用 writeAndFlush()方法以冲刷所有之前写入的
 * 消息
 * ---------------------------------------------------------------------------------------------------------------------
 */
public class HTTPRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String wsUri;
    private static final File INDEX;

    static {
        URL location = HTTPRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to locate index.html", e);
        }
    }

    public HTTPRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {
        //如果请求了WebSocket协议升级,则增加啊引用计数(调用retain方法)并将它传递给下一个ChannelInboundHandler
        if (wsUri.equalsIgnoreCase(request.getUri())) {
            channelHandlerContext.fireChannelRead(request.retain());
        } else {
            //处理100Continue请求http1.1规范
            if (HttpHeaders.is100ContinueExpected(request)) {
                send100Continue(channelHandlerContext);
            }
            //读取index.html
            RandomAccessFile file = new RandomAccessFile(INDEX, "r");
            HttpResponse response = new DefaultFullHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.CONNECTION, "text/plain");
            boolean keepAlive = HttpHeaders.isKeepAlive(request);
            if(keepAlive){//如果请求了Keep-Alive需要添加Http头信息
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,file.length());
                response.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.KEEP_ALIVE);
            }
            //将响应写到客户端
            channelHandlerContext.write(response);
            //将html写到客户端
            if(channelHandlerContext.pipeline().get(SslHandler.class) == null){
                channelHandlerContext.write(new DefaultFileRegion(file.getChannel(),0,file.length()));
            }else {
                channelHandlerContext.write(new ChunkedNioFile(file.getChannel()));
            }
            //写LastHttpContent并且冲刷客户端
            ChannelFuture future = channelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }
}
