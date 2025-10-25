package com.coderlee.concurrent.chapter07.jol;

import org.openjdk.jol.info.ClassLayout;

import lombok.extern.slf4j.Slf4j;

/**
 * ObjectSizeAnalysis 类用于分析 Java 对象的内存布局。
 * 它通过使用 JOL（Java Object Layout）工具来解析和打印对象的内存结构。
 */
@Slf4j
public class ObjectSizeAnalysis {

    /**
     * 主方法，程序入口点。
     * 创建一个 MyObject 实例，并使用 JOL 工具打印该对象的内存布局信息。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        MyObject obj = new MyObject(); // 创建 MyObject 实例
        log.info("{}", ClassLayout.parseInstance(obj).toPrintable()); // 打印对象的内存布局
    }
}