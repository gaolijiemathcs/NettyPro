/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.gao.netty.source.echo2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.Callable;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    // group 就是充当业务线程池 可以将任务提交到改线程池
    // 这里创建了16个线程
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(16);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("EchoServerHandler 的线程是=" + Thread.currentThread().getName());

        // 按原来的解决方案
//        ctx.channel().eventLoop().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5 * 1000);
//                    // 输出线程名
//                    System.out.println("EchoServerHandler execute 线程2是=" + Thread.currentThread().getName());
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端 喵喵喵2", CharsetUtil.UTF_8));
//                } catch (Exception ex) {
//                    System.out.println("发生异常" + ex.getMessage());
//                }
//            }
//        });


        /*
        // 将任务提交到线程池
        group.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // 接受客户端消息
                ByteBuf buf = (ByteBuf) msg;
                byte[] bytes = new byte[buf.readableBytes()];
                buf.readBytes(bytes);
                String body = new String(bytes, "UTF-8");
                Thread.sleep(10 * 1000);
                System.out.println("gourp.submit 的 call 线程是=" + Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端 喵喵喵", CharsetUtil.UTF_8));
                return null;
            }
        });

        // 将任务提交到线程池
        group.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // 接受客户端消息
                ByteBuf buf = (ByteBuf) msg;
                byte[] bytes = new byte[buf.readableBytes()];
                buf.readBytes(bytes);
                String body = new String(bytes, "UTF-8");
                Thread.sleep(10 * 1000);
                System.out.println("gourp.submit 的 call 线程是=" + Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端 喵喵喵", CharsetUtil.UTF_8));
                return null;
            }
        });

        // 将任务提交到线程池
        group.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // 接受客户端消息
                ByteBuf buf = (ByteBuf) msg;
                byte[] bytes = new byte[buf.readableBytes()];
                buf.readBytes(bytes);
                String body = new String(bytes, "UTF-8");
                Thread.sleep(10 * 1000);
                System.out.println("gourp.submit 的 call 线程是=" + Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端 喵喵喵", CharsetUtil.UTF_8));
                return null;
            }
        })；

        */

        //普通方式
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String body = new String(bytes, "UTF-8");
        Thread.sleep(10 * 1000);
        System.out.println("普通方法 的 call 线程是=" + Thread.currentThread().getName());
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端 喵喵喵", CharsetUtil.UTF_8));

        System.out.println("go on");

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
//        cause.printStackTrace();
        ctx.close();
    }
}
