package com.gao.nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress("localhost", 7001));

        String filename = "filename.zip";

        FileChannel fileChannel = new FileInputStream(filename).getChannel();

        // 准备发送
        long startTime = System.currentTimeMillis();

        // 在linux下一个transferTo方法可以完成传输
        // window 每次只能8m
        long transferCnt = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送的总的字节数 = " + transferCnt + " 耗时：" +(System.currentTimeMillis() - startTime));

        fileChannel.close();
    }
}
