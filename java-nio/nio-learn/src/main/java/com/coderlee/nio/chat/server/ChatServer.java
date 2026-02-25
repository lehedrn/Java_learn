package com.coderlee.nio.chat.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * 聊天服务器类
 * 基于Java NIO实现的多人聊天室服务器
 * 支持多个客户端同时连接，实现消息广播功能
 * 使用Reactor模式处理I/O事件
 */
@Slf4j
public class ChatServer {

    /**
     * 程序入口点
     * 创建ChatServer实例并启动服务器
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        try {
            // 创建服务器实例并启动
            new ChatServer().startServer();
        } catch (IOException e) {
            // 将受检异常转换为运行时异常抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 启动聊天服务器
     * 初始化Selector、ServerSocketChannel，并开始监听客户端连接
     * @throws IOException 当I/O操作失败时抛出
     */
    public void startServer() throws IOException {
        // 创建选择器，用于多路复用I/O操作
        Selector selector = Selector.open();
        
        // 创建服务器套接字通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        
        // 绑定端口8000
        ssc.bind(new InetSocketAddress(8000));
        
        // 设置为非阻塞模式
        ssc.configureBlocking(false);
        
        // 注册ACCEPT事件到选择器
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        
        // 记录服务器启动日志
        log.info("server start");
        
        // 主循环：持续监听和处理I/O事件
        for (;;) {
            // 阻塞等待就绪的通道，返回就绪通道数量
            int readChannels = selector.select();
            
            // 如果没有就绪通道，继续下一次循环
            if (readChannels == 0) {
                continue;
            }
            
            // 获取所有就绪的SelectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            
            // 创建迭代器遍历就绪的键
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            
            // 处理每个就绪的SelectionKey
            while (iterator.hasNext()) {
                // 获取当前SelectionKey
                SelectionKey selectionKey = iterator.next();
                
                // 从集合中移除已处理的键，避免重复处理
                iterator.remove();
                
                // 处理新的客户端连接请求
                if (selectionKey.isAcceptable()) {
                    acceptOperator(ssc, selector);
                }
                
                // 处理客户端数据读取请求
                if (selectionKey.isReadable()) {
                    readOperator(selector, selectionKey);
                }
            }
        }
    }

    /**
     * 处理客户端数据读取操作
     * 读取客户端发送的消息并广播给其他客户端
     * @param selector 选择器对象
     * @param selectionKey 当前处理的SelectionKey
     * @throws IOException 当I/O操作失败时抛出
     */
    private void readOperator(Selector selector, SelectionKey selectionKey) throws IOException {
        // 获取关联的SocketChannel
        SocketChannel sc = (SocketChannel) selectionKey.channel();
        
        // 分配1024字节的缓冲区用于读取数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        
        try {
            // 从通道读取数据到缓冲区
            int readLength = sc.read(byteBuffer);
            
            // 客户端正常断开连接
            if (readLength == -1) {
                log.info("客户端 {} 已断开连接", sc.getRemoteAddress());
                // 取消该SelectionKey的注册
                selectionKey.cancel();
                // 关闭SocketChannel
                sc.close();
                return;
            }
            
            // 存储读取到的消息
            String message = "";
            
            // 如果读取到数据，则进行解码处理
            if (readLength > 0) {
                // 切换缓冲区为读模式
                byteBuffer.flip();
                // 将字节数据解码为UTF-8字符串
                message = StandardCharsets.UTF_8.decode(byteBuffer).toString();
                // 清空缓冲区，准备下次使用
                byteBuffer.clear();
            }
            
            // 重新注册读事件，继续监听该通道的数据读取
            sc.register(selector, SelectionKey.OP_READ);
            
            // 如果消息不为空，则广播给其他客户端
            if (!message.isEmpty()) {
                log.info("receive message: {}", message);
                castOtherClient(message, selector, sc);
            }
        } catch (IOException e) {
            // 读取数据时发生异常，通常是客户端异常断开
            log.warn("读取客户端数据时发生异常: {}", e.getMessage());
            // 取消该SelectionKey的注册
            selectionKey.cancel();
            try {
                // 关闭SocketChannel
                sc.close();
            } catch (IOException closeException) {
                log.warn("关闭客户端连接时发生异常: {}", closeException.getMessage());
            }
        }
    }

    /**
     * 向其他客户端广播消息
     * 将收到的消息发送给除发送者外的所有在线客户端
     * @param message 要广播的消息内容
     * @param selector 选择器对象
     * @param sc 发送消息的客户端SocketChannel
     * @throws IOException 当I/O操作失败时抛出
     */
    private void castOtherClient(String message, Selector selector, SocketChannel sc) throws IOException {
        // 获取所有已注册的SelectionKey集合
        Set<SelectionKey> selectionKeySet = selector.keys();
        
        // 创建迭代器遍历所有键
        Iterator<SelectionKey> iterator = selectionKeySet.iterator();
        
        // 遍历所有注册的通道
        while (iterator.hasNext()) {
            // 获取当前SelectionKey
            SelectionKey selectionKey = iterator.next();
            
            // 获取与SelectionKey关联的通道
            Channel targetChannel = selectionKey.channel();
            
            // 只向其他有效的SocketChannel发送消息
            if (targetChannel instanceof SocketChannel && targetChannel != sc) {
                SocketChannel socketChannel = (SocketChannel) targetChannel;
                try {
                    // 检查通道是否仍然有效（开启且已连接）
                    if (socketChannel.isOpen() && socketChannel.isConnected()) {
                        // 将消息编码为UTF-8字节并写入通道
                        socketChannel.write(StandardCharsets.UTF_8.encode(message));
                    } else {
                        // 如果通道已关闭，取消注册并关闭
                        selectionKey.cancel();
                        socketChannel.close();
                        log.info("已移除断开的客户端连接");
                    }
                } catch (IOException e) {
                    // 客户端连接异常，移除该连接
                    log.warn("向客户端发送消息时发生异常: {}", e.getMessage());
                    selectionKey.cancel();
                    try {
                        socketChannel.close();
                    } catch (IOException closeException) {
                        log.warn("关闭客户端连接时发生异常: {}", closeException.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 处理新的客户端连接请求
     * 接受客户端连接，配置为非阻塞模式，并发送欢迎消息
     * @param ssc 服务器套接字通道
     * @param selector 选择器对象
     * @throws IOException 当I/O操作失败时抛出
     */
    private void acceptOperator(ServerSocketChannel ssc, Selector selector) throws IOException {
        try {
            // 接受客户端连接
            SocketChannel sc = ssc.accept();
            
            // 如果成功建立连接
            if (sc != null) {
                // 设置为非阻塞模式
                sc.configureBlocking(false);
                
                // 注册READ事件到选择器
                sc.register(selector, SelectionKey.OP_READ);
                
                // 向新客户端发送欢迎消息
                sc.write(StandardCharsets.UTF_8.encode("欢迎进入聊天室\n"));
                
                // 记录新客户端连接日志
                log.info("新客户端连接: {}", sc.getRemoteAddress());
            }
        } catch (IOException e) {
            // 记录接受连接时的错误日志
            log.error("接受客户端连接时发生异常: {}", e.getMessage());
        }
    }
}
