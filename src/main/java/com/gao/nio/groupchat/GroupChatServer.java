package com.gao.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    // 定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            // 得到选择器
            selector = Selector.open();
            // ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 一定要设置非阻塞这样才能确保是nio的channel
            listenChannel.configureBlocking(false);
            // 将该listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 监听
    public void listen() {
        System.out.println("监听线程：" + Thread.currentThread().getName());
        try {
            // 循环监听
            while(true) {
                // 监听2秒
                int count = selector.select();
                if(count > 0) {
                    // 有事件处理
                    // 遍历得到的selectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()) {
                        // 取出selectionKey
                        SelectionKey key = iterator.next();
                        // 监听到了accept
                        if(key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            // 设置channel非阻塞
                            sc.configureBlocking(false);
                            // 将该sc 注册到selectors
                            sc.register(selector, SelectionKey.OP_READ);
                            // 提示
                            System.out.println(sc.getRemoteAddress() + " 上线 ");
                        }
                        if(key.isReadable()) {
                            // 通道发送read事件 通道可读状态
                            // 处理读
                            readData(key);
                        }
                        // 当前的key删除 防止重复操作处理
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待中...");
                }

            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            // 发生异常处理
        }
    }
    
    // 读取客户端消息
    private void readData(SelectionKey key) {
        // 定义一个SocketChannel
        SocketChannel channel = null;
        try {
            // 得到channel
            channel = (SocketChannel) key.channel();
            // 创建Buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            // 根据count的值做处理
            if(count > 0) {
                // 把缓存区的数据转成字符串
                String msg = new String(buffer.array());
                // 输出该消息
                System.out.println("from 客户端" + msg);

                // 向其他客户端转发消息，专门写一个方法来处理
                sendInfoToOtherClients(msg, channel);
            }

        } catch (Exception e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了...");
                // 取消注册
                key.cancel();
                // 关闭通道
                channel.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    //转发消息给其他客户(通道)
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中....");
        System.out.println("服务器转发数据给客户端线程："+ Thread.currentThread().getName());
        // 遍历所有注册到seletor上的socketChannel并且排除self
        for(SelectionKey key : selector.keys()) {
            // 通过key取出对应的socketChannel通道
            Channel targetChannel = key.channel();
            // 排除自己
            if(targetChannel instanceof SocketChannel && targetChannel != self) {
                // 转型
                SocketChannel dest = (SocketChannel) targetChannel;
                // 将msg 存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer的数据写入通道
                int write = dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        // 创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();

        groupChatServer.listen();
    }
}
