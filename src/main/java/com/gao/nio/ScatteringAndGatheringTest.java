package com.gao.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        // bind port to socket and start
        serverSocketChannel.socket().bind(inetSocketAddress);

        // create buffer
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        SocketChannel socketChannel = serverSocketChannel.accept();

        // assume recive 8bytes from client
        int messageLength = 8;

        while (true) {
            int byteRead = 0;

            while(byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteRead = " + byteRead);
                // use stream print to see position and limit
                Arrays.asList(byteBuffers).stream()
                        .map(buffer -> "position=" + buffer.position()
                                        + ", limit=" + buffer.limit())
                        .forEach(System.out::println);
            }
            // 将所有的buffer进行filp
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());
            // 将数据读出显示到客户端
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            // 将所有的buffer进行clear
            Arrays.asList(byteBuffers).forEach(buffer -> {
                buffer.clear();
            });

            System.out.println("byteRead = " + byteRead + ", byteWrite = " + byteWrite + ", messageLength = " + messageLength);
        }
    }
}
