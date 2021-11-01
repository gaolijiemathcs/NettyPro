package com.gao.netty.dubborpc.provider;

import com.gao.netty.dubborpc.netty.NettyServer;

// ServerBootstrap 启动一个服务提供者 就是NettyServer
public class ServerBootstrap {
    public static void main(String[] args) {
        // 代码待填写
        NettyServer.startServer("127.0.0.1", 7000);
    }
}
