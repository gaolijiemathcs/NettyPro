package com.gao.nio;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception {
        // 创建ServerSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到一个Selector对象
        Selector selector = Selector.open();

        // 绑定一个端口6666在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 把serverSocketChannel 注册到selector 关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("注册后的selectionKey 数量 = " + selector.keys().size());

        // 循环等待客户端连接
        while(true) {
            // 等待1s 如果没有事件发生 则返回
            if(selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒 无连接发生");
                continue;
            }

            // 如果selector.select(1000)返回的>0, 就获取到相关的selectionKey集合
            // 1、如果返回的 >0, 表示已经获取到关注的事件
            // 2、 selector.selectedKeys() 返回Selector关注事件的集合
            //     通过selectionKeys 反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历 Set<SelectionKey> 使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while(keyIterator.hasNext()) {
                // 获取得到的selectionKey
                SelectionKey key = keyIterator.next();

                // 根据key 对应的通道
                // 如果是OP_ACCEPT 有新的客户端连接
                if(key.isAcceptable()) {
                    // 给该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功 生成了一个socketChannel" + socketChannel.hashCode());
                    // 将 SocketChannel 设置为非阻塞
                    socketChannel.configureBlocking(false);
                    // 将当前的socketChannel注册到selector 设置关注事件为OP_READ  同时给socketChannel
                    // 关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("客户端连接后 注册的selectionKey数量=" + selector.keys().size());
                }

                // 如果是OP_READ
                if(key.isReadable()) {
                    // 通过key 反向获取对应的Channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取到该channel 关联的buffer（上面一个判断 每次新增客户端连接的时候都会新建buffer）
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端 " + new String(buffer.array()));

                }

                // 手动从集合中移动当前的selectionKey 防止重复操作
                keyIterator.remove();;
            }


        }
    }
}
