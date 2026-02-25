package com.coderlee.nio.chat.client;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * 客户端消息接收线程类
 * 实现Runnable接口，专门负责在后台线程中接收服务器广播的消息
 * 使用NIO Selector机制实现异步消息接收
 */
@Slf4j
public class ClientThread implements Runnable {

    // 选择器引用，用于监控I/O事件
    private Selector selector;
    
    /**
     * 构造函数
     * @param selector 用于监控通道事件的选择器
     */
    public ClientThread(Selector selector) {
        this.selector = selector;
    }
    
    /**
     * 线程执行方法
     * 持续监听并处理来自服务器的消息
     * 实现了Reactor模式的消息接收循环
     */
    @Override
    public void run() {
        try {
            // 无限循环监听服务器消息
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
                    
                    // 如果是可读事件，则处理消息接收
                    if (selectionKey.isReadable()) {
                        readOperator(selector, selectionKey);
                    }
                }
            }
        } catch (IOException e) {
            // 将受检异常转换为运行时异常抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理消息读取操作
     * 从服务器读取数据并输出到客户端控制台
     * @param selector 选择器对象
     * @param selectionKey 当前处理的SelectionKey
     * @throws IOException 当I/O操作失败时抛出
     */
    private void readOperator(Selector selector, SelectionKey selectionKey) throws IOException {
        // 获取关联的SocketChannel
        SocketChannel sc = (SocketChannel) selectionKey.channel();
        
        // 分配1024字节的缓冲区用于读取数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        
        // 从通道读取数据到缓冲区
        int readLength = sc.read(byteBuffer);
        
        // 存储读取到的消息
        String message = "";
        
        // 如果读取到数据，则进行解码处理
        if (readLength > 0) {
            // 切换缓冲区为读模式
            byteBuffer.flip();
            // 将字节数据解码为UTF-8字符串并追加到消息中
            message += StandardCharsets.UTF_8.decode(byteBuffer).toString();
        }
        
        // 重新注册读事件，继续监听该通道的数据读取
        sc.register(selector, SelectionKey.OP_READ);
        
        // 如果消息不为空，则输出到控制台
        if (!message.isEmpty()) {
            log.info("receive message: {}", message);
        }
    }
}
