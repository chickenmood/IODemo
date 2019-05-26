package com.cuggd.nio.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 网络服务器端程序
 */
public class NIOServer {
    public static void main(String[] args) throws  Exception{
        //1. 得到一个ServerSocketChannel对象  老大
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        //2. 得到一个Selector对象   间谍
        Selector selector=Selector.open();
        //3. 绑定一个端口号
        serverSocketChannel.bind(new InetSocketAddress(9999));
        //4. 设置非阻塞方式
        serverSocketChannel.configureBlocking(false);
        //5. 把ServerSocketChannel对象注册给Selector对象
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //6. 干活（selector监控各个客户端）
        while(true){//服务器端不能停
            //6.1 监控客户端
            if(selector.select(2000)==0){  //nio非阻塞式的优势
                System.out.println("Server:没有客户端搭理我，我就干点别的事");
                continue;
            }
            //6.2 得到SelectionKey,判断通道里的事件
            Iterator<SelectionKey> keyIterator=selector.selectedKeys().iterator();
            while(keyIterator.hasNext()){
                SelectionKey key=keyIterator.next();
                if(key.isAcceptable()){  //处理客户端连接请求事件（OP_ACCEPT）
                    System.out.println("OP_ACCEPT");
                    //连接客户端，得到相应的SocketChannel
                    SocketChannel socketChannel=serverSocketChannel.accept();
                    //设置为非阻塞方式
                    socketChannel.configureBlocking(false);
                    //将此SocketChannel在selector上注册，并表明服务端下次监听的事件为“读取客户端数据事件（OP_READ）”
                    //第三个参数为附件
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if(key.isReadable()){  //处理读取客户端数据事件（OP_READ）
                    //通过key得到服务端需要读取数据的网络通道SocketChannel
                    SocketChannel channel=(SocketChannel) key.channel();
                    //从附件中得到一个缓冲区
                    ByteBuffer buffer=(ByteBuffer) key.attachment();
                    //将通道中的数据读入缓冲区
                    channel.read(buffer);
                    System.out.println("客户端发来数据："+new String(buffer.array()));
                }
                // 6.3 手动从集合中移除当前key,防止重复处理
                keyIterator.remove();
            }
        }
    }
}
