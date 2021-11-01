package com.gao.netty.dubborpc.netty;

import com.gao.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

// 服务器这边的handler比较简单
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 获取客户端发送的消息 并且调用服务
        System.out.println("msg=" + msg);

        // 定义一个协议
        // 客户端在调用服务器api的时候 我们需要定义一个协议 按照服务器的规范去调用
        // 比如我们要求 每次发消息时 都必须以某个字符串开头  例如 必须以这个开头："HelloService#hello#你好"
        if(msg.toString().startsWith("HelloService#hello#")) {
            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}
