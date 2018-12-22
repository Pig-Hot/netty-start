package com.netty.start.http.httpnetty.proxy.common;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class HostInfo {
    private final String HOST_NAME;
    private final String HOST_ADDRESS;

    public HostInfo() {
        String hostName;
        String hostAddress;

        try {
            InetAddress localhost = InetAddress.getLocalHost();

            hostName = localhost.getHostName();
            hostAddress = localhost.getHostAddress();
        } catch (UnknownHostException e) {
            hostName = "localhost";
            hostAddress = "127.0.0.1";
        }

        HOST_NAME = hostName;
        HOST_ADDRESS = hostAddress;
    }

    public final String getName() {
        return HOST_NAME;
    }

    public final String getAddress() {
        return HOST_ADDRESS;
    }


    @Override
    public final String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Host Name : ").append(getName()).append("\n");
        buffer.append("Host Address : ").append(getAddress()).append("\n");
        return buffer.toString();
    }
}