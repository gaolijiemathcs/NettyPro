package com.gao.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;  // 上下文
    private String result;  // 将来调用后返回的结果
    private String para;    // 客户端调用方法时 传入的参数


    // 与服务器的连接创建成功后将会被调用的方法(这个方法被第一个被调用)
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive被调用");
        context = ctx;  // 需要在其他方法中使用当前这个handler的上下文
    }

    // 第四个被调用 调用完以后又会回call
    // 收到服务器的数据后 就会调用的方法
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead被调用");
        result = msg.toString();
        notify(); // notify 唤醒call当时发出去的线程的等待线程
    }

    // 出了异常以后到这里
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


    // 第三个被调用call() wait时调用channelRead 等待notify 然后返回 到现在 执行第五步
    // 被代理对象调用， 发送数据给服务器，-> 发送给服务器以后将waiting 等待被唤醒 （channelRead notify） -> 返回结果
    // synchronized 与channelActive 同步
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("call1 被调用");
        // 发送给服务器的数据 para
        context.writeAndFlush(para);
        // 进行wait()
        wait(); // 等待channelRead获取到服务器的结果以后才唤醒
        System.out.println("call2 被调用");
        return result;  // 服务方返回的结果
    }

    // 第二个被调用
    void setPara(String para) {
        System.out.println("setPara 被调用");
        this.para = para;
    }
}
