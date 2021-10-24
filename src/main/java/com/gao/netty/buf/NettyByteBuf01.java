package com.gao.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args) {

        //创建ByteBuf
        //1.创建对象 改归乡包含一个数组arr 三一个byte[10]
        //2. 在netty中不需要使用flip 进行反转
        //   底层维护了readerIndex 和 writeIndex
        ByteBuf buffer = Unpooled.buffer();

        for(int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        System.out.println("capacity=" + buffer.capacity());

//        for(int i = 0; i < buffer.capacity(); i++) {
//            System.out.println(buffer.getByte(i));
//        }
        for(int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }
        System.out.println("执行完毕");
    }
}
