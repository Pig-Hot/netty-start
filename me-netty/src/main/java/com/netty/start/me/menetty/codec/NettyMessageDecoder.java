package com.netty.start.me.menetty.codec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.netty.start.me.menetty.struct.Header;
import com.netty.start.me.menetty.struct.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Netty消息解码类,继承LengthFieldBasedFrameDecoder解码器，支持自动的TCP粘包和半包处理
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
	
	MarshallingDecoder marshallingDecoder;
	
	/**
     * 标识消息长度的字段偏移量和消息长度自身所占的字节数 
	 * @throws IOException 
     */
	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		marshallingDecoder = new MarshallingDecoder();
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception{
		//System.out.println("start decoding");
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if (frame == null){
			return null;
		}
		
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setCrcCode(frame.readInt());
		header.setLength(frame.readInt());
		header.setSessionID(frame.readLong());
		header.setType(frame.readByte());
		header.setPriority(frame.readByte());
		
		int size = frame.readInt();
		if (size > 0){
			Map<String, Object> attch = new HashMap<String, Object>(size);
			int keySize = 0;
			byte[] keyArray = null;
			String key = null;
			for(int i=0; i<size; ++i){
				keySize = frame.readInt();
				keyArray = new byte[keySize];
				frame.readBytes(keyArray);
				key = new String(keyArray, "UTF-8");
				attch.put(key, marshallingDecoder.decode(frame));
			}
			keyArray = null;
			key = null;
			header.setAttachment(attch);
		}
		if (frame.readableBytes() > 4) {
			message.setBody(marshallingDecoder.decode(frame));
		}
		message.setHeader(header);
		return message;
	}
	

	
}
