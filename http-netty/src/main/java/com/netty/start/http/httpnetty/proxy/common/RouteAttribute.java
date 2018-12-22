package com.netty.start.http.httpnetty.proxy.common;

//netty-route.properties
public class RouteAttribute {
    private String serverPath;
    private String keyWord;
    private String matchAddr;

    public String getMatchAddr() {
        return matchAddr;
    }

    public void setMatchAddr(String matchAddr) {
        this.matchAddr = matchAddr;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}