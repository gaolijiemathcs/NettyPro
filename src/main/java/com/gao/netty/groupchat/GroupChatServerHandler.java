package com.gao.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 使用一个hashMap管理
//    public static Map<String, Channel> channels = new HashMap<String, Channel>();
//    public static Map<User, Channel> channels2 = new HashMap<User, Channel>();

    // 定义一个channel组 管理所有的channel
    // 全局的事件执行器 是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // handlerAdded 当连接建立的时候 一但被连接 第一个被执行
    // 将当前的channel加入到channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户加入聊天的信息推送给其他在线的客户端
        /**
         * 该方法将channelGroup中所有的channel遍历 并且发送 writeAndFlush(str) 中str消息
         */
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 加入聊天" +
            sdf.format(new java.util.Date()) + " \n");
        channelGroup.add(channel);

//        channels.put("id100", channel);
//        channels2.put(new User(10, "123"), channel);
    }

    // 表示断开连接 将xx客户端离开信息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开了\n");
        System.out.println("channelGroup size " + channelGroup.size());
    }

    // 表示channel处于活动的状态 可以提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了");
    }

    // 表示channel处于不活动的状态 表示xx离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了");
    }

    // 读取信息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取到当前的channel
        Channel channel = ctx.channel();
        // 这时我们遍历channelGroup 根据不同的情况 回送不同的消息 自己和其他客户端显示不同
        channelGroup.forEach(ch -> {
            if(channel != ch) {
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送消息" + msg + "\n");
            } else {
                // 自己的channel
                ch.writeAndFlush("[自己]发送了消息" + msg + "\n");
            }
        });
    }

    // 出现异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
    }
}
