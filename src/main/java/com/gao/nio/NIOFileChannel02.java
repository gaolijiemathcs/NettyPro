package com.gao.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception {
        // create a input stream
        File file = new File("./file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        // use fileInputStream get FileChannel -> type : FileChannelImpl
        FileChannel fileChannel = fileInputStream.getChannel();

        // create a buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        // Channel data read into buffer
        fileChannel.read(byteBuffer);

        // change bytes to string info
        System.out.println(new String(byteBuffer.array()));

        fileInputStream.close();
    }
}
