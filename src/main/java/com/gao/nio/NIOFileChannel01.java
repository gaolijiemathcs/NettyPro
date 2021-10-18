package com.gao.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception{
        String str = "hello world";
        // create a output stream -> channel
        FileOutputStream fileOutputStream = new FileOutputStream("./file01.txt");

        // getChannel
        FileChannel fileChannel = fileOutputStream.getChannel();

        // create a buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // str put into buffer
        byteBuffer.put(str.getBytes());

        // before buffer write string -> buffer
        // byteBuffer filp  after filp  buffer data -> channel
        byteBuffer.flip();

        // after flip byteBuffer can write to channel
        fileChannel.write(byteBuffer);

        fileOutputStream.close();
    }
}
