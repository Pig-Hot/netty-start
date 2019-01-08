package netty.zrprotocol.message;

import netty.zrprotocol.message.ZrHeader;

/**
 * Created by zhuran on 2018/12/12 0012
 */
public class ZrMessage {
    private ZrHeader header;
    private String body;

    public ZrMessage(ZrHeader header, String body) {
        this.header = header;
        this.body = body;
    }

    public ZrHeader getHeader() {
        return header;
    }

    public void setHeader(ZrHeader header) {
        this.header = header;
    }

    public String  getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
