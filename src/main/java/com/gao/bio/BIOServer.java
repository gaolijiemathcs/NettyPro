package com.gao.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws Exception {
        // threadPool
        // 1. create a thread pool
        // 2. if there is a client connection request, we will create a thread to communicate with it

        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        // create ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("the server started");

        while(true) {
            System.out.println("线程信息 id = " + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
            System.out.println("等待连接....");
            final Socket socket = serverSocket.accept();
            System.out.println("连接到了一个客户端....");
            System.out.println("connect to a client");
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    // talk to the client
                    handler(socket);
                }
            });
        }
    }

    public static void handler(Socket socket) {

        try {
            System.out.println("线程信息 id = " + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];

            InputStream inputStream = socket.getInputStream();

            while(true) {
                System.out.println("线程信息 id = " + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
                System.out.println("等待读取....");
                int read = inputStream.read(bytes);
                System.out.println("读取到了一个客户端输入...");
                if(read != -1) {
                    System.out.println("线程信息 id = " + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
                    System.out.println(new String(bytes, 0, read));

                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("close socket");

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
