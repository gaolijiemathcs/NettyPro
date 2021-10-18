package com.gao.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        // 创建一个buffer大小为存5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        // 向buffer中存放数据
//        intBuffer.put(10);
//        intBuffer.put(11);
//        intBuffer.put(12);
//        intBuffer.put(13);
//        intBuffer.put(14);
//
        for(int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }

        // 如何从Buffer里面读取数据
        // 将buffer读写切换（）
        intBuffer.flip();

        while(intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
