package com.coderlee.nio.selector;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Selector演示类 - 基于NIO的多路复用器示例
 * 展示了如何使用Selector实现一个简单的服务器端和客户端通信
 * 包含服务器端监听连接、接收数据，以及客户端发送数据的功能
 */
@Slf4j
public class SelectorDemo2 {

    /**
     * 服务器端演示方法
     * 创建一个基于Selector的服务器，可以同时处理多个客户端连接
     * 使用非阻塞模式监听8080端口，接收并打印客户端发送的数据
     */
    @Test
    public void serverDemo() {
        try (
                // 创建服务器套接字通道
                ServerSocketChannel ssc = ServerSocketChannel.open();
                // 创建选择器
                Selector selector = Selector.open()
        ) {
            // 设置为非阻塞模式
            ssc.configureBlocking(false);
            // 绑定到8080端口
            ssc.bind(new InetSocketAddress(8080));

            // 将服务器通道注册到选择器，监听接受连接事件
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            // 持续监听事件
            while (selector.select() > 0) {
                // 获取已就绪的键集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                // 创建迭代器遍历键集合
                Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                while (selectionKeyIterator.hasNext()) {
                    // 获取下一个键
                    SelectionKey next = selectionKeyIterator.next();
                    
                    // 判断是否是接受连接事件
                    if (next.isAcceptable()) {
                        // 接受客户端连接
                        SocketChannel accept = ssc.accept();
                        // 设置客户端通道为非阻塞模式
                        accept.configureBlocking(false);
                        // 将客户端通道注册到选择器，监听读取事件
                        accept.register(selector, SelectionKey.OP_READ);
                    } 
                    // 判断是否是读取事件
                    else if (next.isReadable()) {
                        // 获取触发读取事件的客户端通道
                        try (SocketChannel sc = (SocketChannel) next.channel()) {
                            // 创建缓冲区
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            // 循环读取数据直到没有更多数据
                            while (sc.read(buffer) > 0) {
                                // 翻转缓冲区准备读取
                                buffer.flip();
                                // 打印接收到的数据
                                log.info("收到数据: {}", StandardCharsets.UTF_8.decode(buffer));
                                // 清空缓冲区准备下次读取
                                buffer.clear();
                            }
                        }
                    }

                    // 移除已处理的键，避免重复处理
                    selectionKeyIterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 客户端演示方法
     * 创建一个简单的客户端，连接到服务器并发送当前时间戳
     */
    @Test
    public void clientDemo() {
        try (
            // 创建客户端套接字通道并连接到服务器
            SocketChannel sc = SocketChannel.open(new InetSocketAddress("localhost", 8080))
        ) {
            // 设置为非阻塞模式
            sc.configureBlocking(false);
            // 创建缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // 将当前时间格式化后放入缓冲区
            byteBuffer.put((LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).getBytes(StandardCharsets.UTF_8));
            // 翻转缓冲区准备写入
            byteBuffer.flip();
            // 向服务器发送数据
            sc.write(byteBuffer);
            // 清空缓冲区
            byteBuffer.clear();
            // 记录发送的日志
            log.info("发送数据: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 主方法 - 交互式客户端
     * 允许用户通过控制台输入数据并发送给服务器
     * 每次输入都会附带时间戳前缀
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        try (
            // 创建客户端套接字通道并连接到服务器
            SocketChannel sc = SocketChannel.open(new InetSocketAddress("localhost", 8080))
        ) {
            // 设置为非阻塞模式
            sc.configureBlocking(false);
            // 创建缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // 创建控制台输入扫描器
            Scanner scanner = new Scanner(System.in);
            
            // 持续监听用户输入
            while (scanner.hasNext()) {
                // 构造包含时间戳的消息内容
                String content = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " ---> " + scanner.next();
                // 将消息内容转换为字节数组并放入缓冲区
                byteBuffer.put(content.getBytes(StandardCharsets.UTF_8));
                // 翻转缓冲区准备写入
                byteBuffer.flip();
                // 向服务器发送数据
                sc.write(byteBuffer);
                // 清空缓冲区准备下次使用
                byteBuffer.clear();
                // 记录发送的日志
                log.info("发送数据: {}", content);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
