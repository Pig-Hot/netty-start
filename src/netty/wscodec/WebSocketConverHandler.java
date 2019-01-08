package netty.wscodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

/**
 * Created by zhuran on 2018/12/11 0011
 */
public class WebSocketConverHandler extends MessageToMessageCodec<WebSocketFrame, MyWebSocketFrame> {
    protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.getData().duplicate().retain();
        switch (msg.getType()) {
            case PING:
                out.add(new PingWebSocketFrame(payload));
            case BINARY:
                out.add(new BinaryWebSocketFrame(payload));
            case PONG:
                out.add(new PongWebSocketFrame(payload));
            case TEXT:
                out.add(new TextWebSocketFrame(payload));
            case CLOSE:
                out.add(new CloseWebSocketFrame(true, 0, payload));
            case CONTINUATION:
                out.add(new ContinuationWebSocketFrame(payload));
            default:
                throw new Exception(String.valueOf(msg));
        }
    }

    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.content().duplicate().retain();
        if (msg instanceof BinaryWebSocketFrame) {
            out.add(new MyWebSocketFrame(
                    MyWebSocketFrame.FrameType.BINARY, payload));
        } else if (msg instanceof CloseWebSocketFrame) {
            out.add(new MyWebSocketFrame(
                    MyWebSocketFrame.FrameType.CLOSE, payload));
        } else if (msg instanceof PingWebSocketFrame) {
            out.add(new MyWebSocketFrame(
                    MyWebSocketFrame.FrameType.PING, payload));
        } else if (msg instanceof PongWebSocketFrame) {
            out.add(new MyWebSocketFrame(
                    MyWebSocketFrame.FrameType.PONG, payload));
        } else if (msg instanceof TextWebSocketFrame) {
            out.add(new MyWebSocketFrame(
                    MyWebSocketFrame.FrameType.TEXT, payload));
        } else if (msg instanceof ContinuationWebSocketFrame) {
            out.add(new MyWebSocketFrame(
                    MyWebSocketFrame.FrameType.CONTINUATION, payload));
        } else {
            throw new IllegalStateException(
                    "Unsupported websocket msg " + msg);
        }
    }
}
