package com.netty.start.me.menetty.codec;

import java.io.IOException;

import org.jboss.marshalling.ByteOutput;

import io.netty.buffer.ByteBuf;

public class ChannelBufferByteOutput implements ByteOutput{
	
	private final ByteBuf buffer;

	public ChannelBufferByteOutput(ByteBuf buffer) {
		this.buffer = buffer;
	}

	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void flush() throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void write(int b) throws IOException {
		buffer.writeByte(b);
	}

	public void write(byte[] b) throws IOException {
		buffer.writeBytes(b);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		buffer.writeBytes(b, off, len);
		
	}
	
	public ByteBuf getBuffer() {
		return buffer;
	}
	
}
