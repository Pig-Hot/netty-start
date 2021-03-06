package com.netty.start.rpc.rpcnetty.sample_client;

import com.netty.start.rpc.rpcnetty.client.RpcProxy;
import com.netty.start.rpc.rpcnetty.sample_api.HelloService;
import com.netty.start.rpc.rpcnetty.sample_api.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloClient2 {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-client.xml");
        RpcProxy rpcProxy = context.getBean(RpcProxy.class);

        HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.hello(new Person("Yong", "Huang"));
        System.out.println(result);

        System.exit(0);
    }
}
