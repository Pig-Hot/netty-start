package com.netty.start.rpc.rpcnetty.sample_server;

import com.netty.start.rpc.rpcnetty.sample_api.HelloService;
import com.netty.start.rpc.rpcnetty.sample_api.Person;
import com.netty.start.rpc.rpcnetty.server.RpcService;

@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hello! " + person.getFirstName() + " " + person.getLastName();
    }
}
