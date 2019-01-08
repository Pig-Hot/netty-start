package com.netty.start.rpc.rpcnetty.sample_api;

public interface HelloService {

    String hello(String name);

    String hello(Person person);
}
