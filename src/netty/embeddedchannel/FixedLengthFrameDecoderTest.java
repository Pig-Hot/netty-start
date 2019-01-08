package netty.embeddedchannel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertNull;

/**
 * Created by zhuran on 2018/12/10 0010
 */
public class FixedLengthFrameDecoderTest {
    @Test
    public void testFrameDecode() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));
        Assert.assertTrue(channel.writeInbound(input.retain()));
        Assert.assertTrue(channel.finish());
        ByteBuf read = channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();
        read = channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();
        read = channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();
        assertNull(channel.readInbound());
        buf.release();
    }
}
