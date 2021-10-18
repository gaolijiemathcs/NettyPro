package com.gao.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while(true) {
            // clean buffer
            byteBuffer.clear();
            // data fileChannel01 -> byteBuffer
            int read = fileChannel01.read(byteBuffer);
            System.out.println("read=" + read);
            if(read == -1) {
                // finishi read;
                break;
            }
            // buffer data -> fileChannel02 -- 2.txt
            // buffer fist is read status  after flip() buffer status is write status.
            byteBuffer.flip();
            fileChannel02.write(byteBuffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }
}
