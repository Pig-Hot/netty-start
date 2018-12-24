package com.netty.start.me.menetty.struct;

/**
 * 消息体定义
 */
public class NettyMessage {

    private Header header;

    private Object body;

    public final Header getHeader() {
    	return header;
    }

    public final void setHeader(Header header) {
    	this.header = header;
    }

    public  Object getBody() {
    	return body;
    }
    
    public final void setBody(Object body) {
    	this.body = body;
    }

    @Override
    public String toString() {
    	return "NettyMessage [header=" + header + "]";
    }
}
