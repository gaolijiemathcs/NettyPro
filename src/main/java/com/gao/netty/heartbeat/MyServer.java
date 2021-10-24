package com.gao.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {
    public static void main(String[] args) throws Exception{
        // 事件循环组 两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();// 默认8个nioEventLoop

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 加入一个netty提供IdleStateHandler
                            /**
                             * 1. IdleStateHandler 是Netty提供的处理空闲状态的处理器
                             * 2. long readerIdleTime 表示多长事件没有读 会发送一个心跳检测包 检测是否还是连接状态
                             * 3. long writerIdleTime 表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                             * 4. long allIdleTime 表示多长时间没有读写 会发送一个心跳检测包检测是否连接
                             * 5. 文档说明Triggers an IdleStateEvent when a Channel has not performed read, write, or both operation for a while.
                             * Supported idle states
                             * 6. 当IdleStateEvent 触发以后 会传递给管道的下一个handler处理
                             * 通过调用(触发)下一个handler的userEventTriggered 在该方法中区处理
                             * 可能是读空闲/写空闲/读写空闲
                             */
                            pipeline.addLast(new IdleStateHandler(13, 5, 2, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的自定义handler(自定义)
                            pipeline.addLast(new MyServerHandler());

                        }
                    });
            // 启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
          bossGroup.shutdownGracefully();
          workerGroup.shutdownGracefully();
        }
    }
}
