package com.coderlee.nio.channel;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * DatagramChannel演示类
 * 展示了Java NIO中DatagramChannel的基本用法，包括UDP数据包的发送和接收
 * DatagramChannel是用于UDP网络通信的通道，支持无连接的数据报传输
 * 
 * 主要功能：
 * 1. UDP数据包发送测试
 * 2. UDP数据包接收测试  
 * 3. 连接模式下的双向通信测试
 * 
 * 注意：所有测试方法都是无限循环，需要手动停止程序
 */
@Slf4j
public class DatagramChannelDemo {

    /**
     * 测试UDP数据包发送功能
     * 创建DatagramChannel并持续向指定地址发送包含当前时间的UDP数据包
     * 发送频率：每秒一次
     * 目标地址：localhost:9999
     */
    @Test
    public void testSendDatagram() {
        // 打开一个新的DatagramChannel
        try (DatagramChannel sendChannel = DatagramChannel.open()) {
            // 设置目标地址为本地9999端口
            InetSocketAddress sendAddress = new InetSocketAddress("localhost", 9999);

            // 无限循环发送数据
            while (true) {
                // 创建包含当前时间的字节缓冲区
                ByteBuffer buffer = ByteBuffer.wrap(("现在是: " + LocalDateTime.now()).getBytes(StandardCharsets.UTF_8));
                // 发送数据到指定地址
                sendChannel.send(buffer, sendAddress);
                // 记录发送日志
                log.info("发送数据: {}", "现在是: " + LocalDateTime.now());
                // 休眠1秒钟
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (IOException | InterruptedException e) {
            // 将检查异常转换为运行时异常抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试UDP数据包接收功能
     * 创建DatagramChannel并绑定到9999端口，持续接收UDP数据包
     * 接收缓冲区大小：1024字节
     */
    @Test
    public void receiveDatagram() {
        // 打开一个新的DatagramChannel
        try (DatagramChannel receiveChannle = DatagramChannel.open()) {
            // 绑定到9999端口监听数据
            InetSocketAddress receiveAddress = new InetSocketAddress(9999);
            receiveChannle.bind(receiveAddress);
            // 分配1024字节的接收缓冲区
            ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);

            // 无限循环接收数据
            while (true) {
                // 清空缓冲区准备接收新数据
                receiveBuffer.clear();
                // 接收数据包，返回发送方地址
                SocketAddress socketAddress = receiveChannle.receive(receiveBuffer);
                // 翻转缓冲区准备读取
                receiveBuffer.flip();
                // 记录接收到的数据和发送方地址
                log.info("收到 {} 数据: {}", socketAddress.toString(), new String(receiveBuffer.array(), StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            // 将检查异常转换为运行时异常抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试连接模式下的双向通信
     * DatagramChannel在连接模式下可以使用read/write方法进行双向通信
     * 绑定本地端口9999并连接到localhost:9999（自连接）
     * 先发送一条消息，然后持续接收回复
     */
    @Test
    public void testSendAndReceive() {
        // 打开一个新的DatagramChannel
        try (DatagramChannel connChannel = DatagramChannel.open()) {
            // 绑定到本地9999端口
            connChannel.bind(new InetSocketAddress(9999));
            // 连接到目标地址（这里是自身）
            connChannel.connect(new InetSocketAddress("localhost", 9999));
            // 发送初始消息
            connChannel.write(ByteBuffer.wrap(("hello coderlee, time: " + LocalDateTime.now()).getBytes(StandardCharsets.UTF_8)));
            // 分配128字节的读取缓冲区
            ByteBuffer readBuffer = ByteBuffer.allocate(128);
            
            // 无限循环读取数据
            while (true) {
                // 清空缓冲区准备读取新数据
                readBuffer.clear();
                // 从通道读取数据
                connChannel.read(readBuffer);
                // 翻转缓冲区准备读取
                readBuffer.flip();
                // 记录接收到的数据
                log.info("收到数据: {}", new String(readBuffer.array(), StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            // 将检查异常转换为运行时异常抛出
            throw new RuntimeException(e);
        }
    }
}
