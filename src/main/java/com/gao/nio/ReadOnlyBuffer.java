package com.gao.nio;

import java.nio.ByteBuffer;

public class ReadOnlyBuffer {
    public static void main(String[] args) {
        // create a buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            byteBuffer.put((byte) i);
        }

        // read
        byteBuffer.flip();

        // get a readOnly method
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        while( readOnlyBuffer.hasRemaining() ) {
            System.out.println(readOnlyBuffer.get());
        }

        readOnlyBuffer.put((byte) 100); // readonlybuffer exception
    }
}
