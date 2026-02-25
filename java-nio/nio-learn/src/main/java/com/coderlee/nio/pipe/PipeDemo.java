package com.coderlee.nio.pipe;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * Pipe管道演示类
 * 展示了Java NIO中Pipe的使用方法
 * Pipe是一种单向的数据传输通道，包含SinkChannel（写入端）和SourceChannel（读取端）
 * 常用于线程间通信或数据流处理
 */
@Slf4j
public class PipeDemo {

    /**
     * 主方法 - 演示Pipe的基本使用
     * 创建Pipe管道，通过SinkChannel写入数据，通过SourceChannel读取数据
     * 展示了NIO中管道通信的完整流程
     */
    public static void main(String[] args) {
        try {
            // 打开一个新的Pipe管道
            Pipe pipe = Pipe.open();
            
            // 获取管道的写入端（SinkChannel）
            Pipe.SinkChannel sinkChannel = pipe.sink();
            
            // 创建缓冲区用于数据传输
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            
            // 将字符串数据放入缓冲区
            buffer.put("hello coderlee".getBytes());
            
            // 翻转缓冲区，准备写入操作
            buffer.flip();
            
            // 通过SinkChannel将数据写入管道
            sinkChannel.write(buffer);
            
            // 记录写入日志
            log.info("写入数据: {}", "hello coderlee");

            // 获取管道的读取端（SourceChannel）
            Pipe.SourceChannel sourceChannel = pipe.source();
            
            // 创建新的缓冲区用于读取数据
            ByteBuffer readButter = ByteBuffer.allocate(1024);
            
            // 从SourceChannel读取数据到缓冲区，返回实际读取的字节数
            int length = sourceChannel.read(readButter);
            
            // 将读取的字节数据转换为字符串并记录日志
            log.info("读取数据: {}", new String(readButter.array(), 0, length));

            // 关闭读取端通道
            sourceChannel.close();
            
            // 关闭写入端通道
            sinkChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
