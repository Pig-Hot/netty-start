package com.netty.start.me.menetty.codec;

import java.io.IOException;

import org.jboss.marshalling.ByteInput;

import io.netty.buffer.ByteBuf;

/**
 *  ChannelBufferByteInput类
 */
public class ChannelBufferByteInput implements ByteInput{

	private final ByteBuf buffer;
	
	public ChannelBufferByteInput(ByteBuf buffer) {
		this.buffer = buffer;
	}

	public void close() throws IOException {
		// TODO Auto-generated method stub
	}

	public int read() throws IOException {
		if(buffer.isReadable()){
			return buffer.readByte() & 0xff;
		}
		return -1;
	}

	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	public int read(byte[] b, int off, int len) throws IOException {
		int available = available();
		if (available == 0){
			return -1;
		}
		len = Math.min(available, len);
		buffer.readBytes(b, off, len);
		return len;
	}

	public int available() throws IOException {
		return buffer.readableBytes();
	}

	public long skip(long bytes) throws IOException {
		int readable = buffer.readableBytes();
		if (readable < bytes){
			bytes = readable;
		}
		buffer.readerIndex((int)(buffer.readerIndex() + bytes));
		return bytes;
	}

}
