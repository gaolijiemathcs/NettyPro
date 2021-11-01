package com.gao.netty.dubborpc.provider;

import com.gao.netty.dubborpc.publicInterface.HelloService;

public class HelloServiceImpl implements HelloService {

    private int count = 0;

    // 当有消费方 调用该方法时 就返回一个结果
    @Override
    public String hello(String mes) {
        System.out.println("收到客户端消息=" + mes);
        // 根据mes返回不同的结果
        if(mes != null) {
            return "你好客户端，我已经收到你的消息[" + mes + "]第" + (++count) + "次";
        } else {
            return "你好客户端，为已经收到你的消息";
        }
    }
}
