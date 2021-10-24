package com.gao.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        // 创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", Charset.forName("UTF-8"));
        // 使用相关的方法
        if(byteBuf.hasArray()) {
            byte[] content = byteBuf.array();
            // 将content转成字符串
            System.out.println(new String(content, Charset.forName("UTF-8")));
            System.out.println("byteBuf=" + byteBuf);
            System.out.println(byteBuf.arrayOffset()); // 0
            System.out.println(byteBuf.readerIndex()); // 0
            System.out.println(byteBuf.writerIndex()); // 12
            System.out.println(byteBuf.capacity()); // 36

//            System.out.println(byteBuf.readByte()); // 读取1个 h的askii为104
            System.out.println(byteBuf.getByte(0));
            int len = byteBuf.readableBytes();  // 可读的字节数
            System.out.println(len);

            for (int i = 0; i < len; i++) {
                System.out.println((char) byteBuf.getByte(i));
            }

            System.out.println(byteBuf.getCharSequence(0, 4, Charset.forName("UTF-8")));
            System.out.println(byteBuf.getCharSequence(4, 6, Charset.forName("UTF-8")));
        }
    }
}
