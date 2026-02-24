package com.coderlee.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileChannelDemo1 {
    /**
     * 主函数：使用NIO方式读取指定文件内容并输出
     * 通过RandomAccessFile获取文件通道，利用ByteBuffer循环读取文件数据，
     * 使用UTF-8编码解码后打印文件内容。包含完整的资源管理和异常处理。
     *
     * @param args 命令行参数（本方法未使用）
     */
    public static void main(String[] args) {
        log.info("file read begin");
        // 打开文件并初始化NIO读取环境
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(FileChannelDemo1.class.getResource("/file1.text").toURI().getPath(), "rw")) {
            // 获取文件通道进行NIO操作
            FileChannel channel = randomAccessFile.getChannel();
            // 创建1024字节容量的缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int bytesRead = channel.read(buf);
            // 循环读取文件直到末尾
            while (bytesRead != -1) {
                log.info("read: {}", bytesRead);
                // 切换缓冲区至读取模式
                buf.flip();
                // 将缓冲区数据解码为UTF-8字符串
                String content = StandardCharsets.UTF_8.decode(buf).toString();
                log.info("content: {}", content);
                
                // 重置缓冲区准备下一次读取
                buf.clear();
                bytesRead = channel.read(buf);
            }
            buf.clear();
            // channel.close();
            log.info("file read complete");
        } catch (IOException | URISyntaxException e) {
            log.error("error", e);
        }
    }
}
