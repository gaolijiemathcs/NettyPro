package com.gao.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception {
        // stream
        FileInputStream fileInputStream = new FileInputStream("a.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("b.jpg");

        // get stream => channel
        FileChannel sourceCh = fileInputStream.getChannel();
        FileChannel destCh = fileOutputStream.getChannel();

        // use transferForm finish copy
        destCh.transferFrom(sourceCh, 0, sourceCh.size());

        // close related channel and stream
        sourceCh.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
