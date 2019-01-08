package netty.zrprotocol.message;

/**
 * Created by zhuran on 2018/12/12 0012
 */
public class ZrHeader {
    private int version;
    private int contentLength;
    private String sessionId;

    public ZrHeader(int version, int contentLength, String sessionId) {
        this.version = version;
        this.contentLength = contentLength;
        this.sessionId = sessionId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
