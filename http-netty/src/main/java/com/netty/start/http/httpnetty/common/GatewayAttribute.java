package com.netty.start.http.httpnetty.common;

//netty-gateway.properties
public class GatewayAttribute {
    private String serverPath;
    private String defaultAddr;

    public String getDefaultAddr() {
        return defaultAddr;
    }

    public void setDefaultAddr(String defaultAddr) {
        this.defaultAddr = defaultAddr;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }
}