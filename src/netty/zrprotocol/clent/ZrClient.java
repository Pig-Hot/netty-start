package netty.zrprotocol.clent;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.zrprotocol.handler.ZrClientInitalizer;
import netty.zrprotocol.message.ZrHeader;
import netty.zrprotocol.message.ZrMessage;

import java.util.UUID;

/**
 * Created by zhuran on 2018/12/12 0012
 */
public class ZrClient {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .handler(new ZrClientInitalizer());

            // Start the connection attempt.
            Channel ch = null;
            try {
                ch = b.connect("127.0.0.1", 8899).sync().channel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int version = 1;
            String sessionId = UUID.randomUUID().toString();
            String content = "I'm the zr protocol!";

            ZrHeader header = new ZrHeader(version, content.length(), sessionId);
            ZrMessage message = new ZrMessage(header, content);
            assert ch != null;
            ch.writeAndFlush(message);
            ch.close();
        } finally {
            group.shutdownGracefully();
        }
    }
}
