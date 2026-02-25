package com.coderlee.nio.filelock;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 文件锁演示类
 * 展示如何使用Java NIO的FileLock机制来实现文件锁定和并发访问控制
 * 主要功能包括：
 * 1. 获取文件的独占锁
 * 2. 向文件追加数据
 * 3. 读取并显示文件内容
 *
 * @author coderlee
 * @version 1.0
 * @since 2026-02-25
 */
@Slf4j
public class FileLockDemo {
    
    /**
     * 程序入口点
     * 演示文件锁的基本使用流程：
     * 1. 创建ByteBuffer包装要写入的数据
     * 2. 构建文件路径
     * 3. 打开文件通道（写入+追加模式）
     * 4. 获取文件锁
     * 5. 写入数据到文件
     * 6. 读取并显示文件内容
     * 
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 定义要写入文件的内容
        String input = "hello, coderlee";
        log.info("准备写入内容: {}", input);

        // 将字符串转换为字节缓冲区
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());

        // 获取项目根目录并构建文件路径
        String projectRoot = System.getProperty("user.dir");
        String filePath = projectRoot + "/java-nio/nio-learn/src/main/resources/file6.text";
        Path path = Paths.get(filePath);

        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
            // 获取文件当前大小，用于定位写入位置
            long size = channel.size();
            // 如果文件不为空，则将位置设置到文件末尾
            if (size > 0) {
                channel.position(channel.size() - 1);
            }

            // 获取文件的独占锁（阻塞直到获得锁）
            FileLock lock = channel.lock();
            // 共享锁不能用在写操作上
//            FileLock lock = channel.lock(0, Long.MAX_VALUE, true);
            log.info("获取的锁是否为共享锁: {}", lock.isShared());

            // 将数据写入文件
            channel.write(buffer);

            // 读取并显示文件内容
            readFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException("文件操作失败", e);
        }
    }

    /**
     * 读取指定文件的内容并打印到日志
     * 使用BufferedReader逐行读取文件内容
     * 
     * @param filePath 要读取的文件路径
     * @throws RuntimeException 当文件读取发生IO异常时抛出
     */
    private static void readFile(String filePath) {
        try (FileReader fileReader = new FileReader(filePath)) {
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line = bufferedReader.readLine();
                log.info("开始读取文件内容:");
                // 循环读取每一行直到文件结束
                while (line != null) {
                    log.info("文件内容: {}", line);
                    line = bufferedReader.readLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("读取文件失败: " + filePath, e);
        }
    }
}
