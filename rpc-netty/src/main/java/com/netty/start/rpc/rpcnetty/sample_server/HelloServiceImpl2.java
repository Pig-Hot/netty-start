package com.netty.start.rpc.rpcnetty.sample_server;


import com.netty.start.rpc.rpcnetty.sample_api.HelloService;
import com.netty.start.rpc.rpcnetty.sample_api.Person;
import com.netty.start.rpc.rpcnetty.server.RpcService;

@RpcService(value = HelloService.class, version = "sample.hello2")
public class HelloServiceImpl2 implements HelloService {

    @Override
    public String hello(String name) {
        return "你好! " + name;
    }

    @Override
    public String hello(Person person) {
        return "你好! " + person.getFirstName() + " " + person.getLastName();
    }
}
