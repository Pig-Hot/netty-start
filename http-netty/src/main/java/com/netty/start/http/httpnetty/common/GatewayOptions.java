package com.netty.start.http.httpnetty.common;

import io.netty.util.Signal;

import java.nio.charset.Charset;

public class GatewayOptions {
    public static final Charset GATEWAY_OPTION_CHARSET = Charset.forName("GBK");
    public static final Signal GATEWAY_OPTION_SERVICE_ACCESS_ERROR = Signal.valueOf("[NettyGateway]:Access gateway fail!");
    public static final Signal GATEWAY_OPTION_TASK_POST_ERROR = Signal.valueOf("[NettyGateway]:Http post fail!");
    public static final int GATEWAY_OPTION_PARALLEL = Math.max(2, Runtime.getRuntime().availableProcessors());
    public static final int GATEWAY_OPTION_HTTP_POST = 60 * 1000;
    public static final String GATEWAY_OPTION_GATEWAY_CONFIG_FILE = "netty-gateway.properties";
    public static final String GATEWAY_OPTION_ROUTE_CONFIG_FILE = "netty-route.properties";
    public static final String GATEWAY_OPTION_KEY_WORD_SPLIT = ",";
    public static final String GATEWAY_OPTION_SERVER_SPLIT = "@";
    public static final String GATEWAY_OPTION_LOCALHOST = "http://127.10.0.1:8080/";
    public static final String GATEWAY_PROPERTIES_PREFIX_KEY_WORD = ".keyWord";
    public static final String GATEWAY_PROPERTIES_PREFIX_MATCH_ADDR = ".matchAddr";
    public static final String GATEWAY_PROPERTIES_PREFIX_DEFAULT_ADDR = ".defaultAddr";
    public static final String GATEWAY_PROPERTIES_PREFIX_SERVER_PATH = ".serverPath";
    public static final String GATEWAY_PROPERTIES_KEY_WORD = GATEWAY_PROPERTIES_PREFIX_KEY_WORD.substring(1);
    public static final String GATEWAY_PROPERTIES_MATCH_ADDR = GATEWAY_PROPERTIES_PREFIX_MATCH_ADDR.substring(1);
    public static final String GATEWAY_PROPERTIES_SERVER_PATH = GATEWAY_PROPERTIES_PREFIX_SERVER_PATH.substring(1);
    public static final String GATEWAY_PROPERTIES_DEFAULT_ADDR = GATEWAY_PROPERTIES_PREFIX_DEFAULT_ADDR.substring(1);

    private int gatewayPort = 0;

    public int getGatewayPort() {
        return gatewayPort;
    }

    public void setGatewayPort(int gatewayPort) {
        this.gatewayPort = gatewayPort;
    }
}
