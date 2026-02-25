package com.coderlee.nio.chat.client;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 聊天客户端类
 * 基于Java NIO实现的聊天室客户端
 * 支持用户输入消息并通过网络发送到服务器
 * 使用单独线程处理服务器消息接收
 */
@Slf4j
public class ChatClient {
    
    /**
     * 启动聊天客户端
     * 连接到服务器，配置NIO通道，并启动消息收发功能
     * @param name 用户昵称
     * @throws IOException 当网络连接或I/O操作失败时抛出
     */
    public void startClient(String name) throws IOException {
        // 连接到本地8000端口的聊天服务器
        SocketChannel sc = SocketChannel.open(new InetSocketAddress("localhost", 8000));

        // 创建选择器用于处理异步I/O操作
        Selector selector = Selector.open();
        
        // 设置SocketChannel为非阻塞模式
        sc.configureBlocking(false);
        
        // 注册读事件到选择器，用于接收服务器消息
        sc.register(selector, SelectionKey.OP_READ);

        // 启动独立线程处理服务器消息接收
        new Thread(new ClientThread(selector)).start();

        // 创建控制台输入扫描器
        Scanner scanner = new Scanner(System.in);
        
        // 持续监听用户输入
        while (scanner.hasNext()) {
            // 读取用户输入的一行消息
            String msg = scanner.nextLine();
            
            // 如果消息不为空，则发送到服务器
            if (!msg.isEmpty()) {
                // 将用户名和消息组合后编码为UTF-8字节流发送
                sc.write(StandardCharsets.UTF_8.encode(name + ": " + msg));
            }
        }
    }

    /**
     * 程序入口点
     * 创建ChatClient实例并启动客户端
     * @param args 命令行参数，第一个参数作为用户名
     */
    public static void main(String[] args) {
        try {
            // 检查命令行参数是否提供用户名
            if (args.length == 0) {
                log.error("请提供用户名作为命令行参数");
                return;
            }
            
            // 创建客户端实例并使用第一个参数作为用户名启动
            new ChatClient().startClient(args[0]);
        } catch (IOException e) {
            // 记录连接错误日志
            log.error("客户端启动失败", e);
        }
    }
}
