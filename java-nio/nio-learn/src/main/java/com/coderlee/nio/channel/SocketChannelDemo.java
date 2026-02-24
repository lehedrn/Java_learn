/**
 * SocketChannel演示类
 * 展示了如何使用NIO的SocketChannel进行非阻塞网络通信
 * 该示例连接到百度服务器的80端口，并尝试读取数据
 */
package com.coderlee.nio.channel;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Slf4j
public class SocketChannelDemo {
    /**
     * 主方法 - 演示SocketChannel的基本用法
     * 创建SocketChannel连接到指定地址，配置为非阻塞模式，并尝试读取数据
     */
    public static void main(String[] args) {
        // 使用try-with-resources语法自动关闭SocketChannel资源
        try (SocketChannel sc = SocketChannel.open(new InetSocketAddress("www.baidu.com", 80))) {
            // 配置SocketChannel为非阻塞模式
            // 在非阻塞模式下，read()方法会立即返回，即使没有数据可读
            sc.configureBlocking(false);
            
            // 注释掉的代码：配置为阻塞模式
            // sc.configureBlocking(true);
            
            // 分配16字节的ByteBuffer用于接收数据
            ByteBuffer byteBuffer = ByteBuffer.allocate(16);
            
            // 尝试从SocketChannel读取数据到缓冲区
            // 在非阻塞模式下，如果没有数据可读，该方法会立即返回0或-1
            sc.read(byteBuffer);
            
            // 记录读取操作完成的日志信息（如果是阻塞模式，则不会打印此日志）
            log.info("read over");
        } catch (IOException e) {
            // 捕获并重新抛出IO异常
            throw new RuntimeException(e);
        }
    }
}
