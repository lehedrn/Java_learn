package com.coderlee.nio.channel;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * ServerSocketChannel演示类
 * 展示了如何使用NIO的ServerSocketChannel创建非阻塞服务器
 * 该服务器监听指定端口，接收客户端连接并发送预定义数据
 * 
 * 主要特性：
 * - 使用非阻塞模式运行
 * - 循环监听客户端连接
 * - 向连接的客户端发送固定消息
 * - 自动关闭客户端连接
 * 
 * @author coderlee
 * @version 1.0
 */
@Slf4j
public class ServerScoketChannelDemo {

    /**
     * 程序入口点
     * 创建并启动NIO服务器
     * 
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 定义服务器监听端口
        int port = 8888;
        // 准备要发送给客户端的数据
        String data = "hello coderlee";
        // 将字符串数据包装成ByteBuffer缓冲区
        ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());

        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
            // 绑定服务器到指定端口
            ssc.socket().bind(new InetSocketAddress(port));
            // 设置为非阻塞模式
            ssc.configureBlocking(false);
            
            // 持续监听连接请求
            while (true) {
                log.info("等待连接...");
                // 尝试接受客户端连接（非阻塞模式下可能返回null）
                SocketChannel sc = ssc.accept();
                
                // 如果没有客户端连接
                if (null == sc) {
                    log.info("没有连接");
                    // 等待2秒后继续尝试
                    TimeUnit.SECONDS.sleep(2);
                } else {
                    // 成功接收到客户端连接
                    log.info("Incoming connection from: {}", sc.socket().getRemoteSocketAddress());
                    // 重置缓冲区位置到开始位置
                    buffer.rewind();
                    // 向客户端写入数据
                    sc.write(buffer);
                    // 关闭客户端连接
                    sc.close();
                }
            }
        } catch (IOException | InterruptedException e) {
            // 记录异常信息
            log.error("error", e);
        }

    }
}
