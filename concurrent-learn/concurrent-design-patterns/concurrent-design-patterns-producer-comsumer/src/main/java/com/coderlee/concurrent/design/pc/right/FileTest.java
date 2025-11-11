package com.coderlee.concurrent.design.pc.right;

import java.nio.charset.StandardCharsets;

/**
 * 文件处理测试类，演示生产者-消费者模式的使用
 */
public class FileTest {

    /**
     * 主函数，测试文件处理流程
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建文件处理器
        FileProcessor fileProcessor = new FileProcessor();

        // 启动索引处理线程
        fileProcessor.startIndexThread();

        // 创建并上传第一个文件
        String fileName1 = "coderlee001";
        FileInfo fileInfo1 = new FileInfo(fileName1, fileName1, fileName1.getBytes(StandardCharsets.UTF_8));
        fileProcessor.uploadFile(fileInfo1);

        // 创建并上传第二个文件
        String fileName2 = "coderlee002";
        FileInfo fileInfo2 = new FileInfo(fileName2, fileName2, fileName2.getBytes(StandardCharsets.UTF_8));
        fileProcessor.uploadFile(fileInfo2);

        // 关闭索引处理线程
        fileProcessor.shutdownIndexThread();
    }
}
