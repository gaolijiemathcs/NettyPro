package com.gao.netty.dubborpc.publicInterface;

// 这个是接口 是 服务提供方和服务消费方 都需要的
public interface HelloService {
    String hello(String mes);
}
