package com.coderlee.concurrent.chapter04;

import lombok.extern.slf4j.Slf4j;

/**
 * CPU执行顺序演示类
 * 
 * <p>该类用于演示CPU指令执行的基本顺序和程序执行流程。
 * 虽然代码本身很简单，但它展示了程序按顺序执行指令的基本原理。
 * 在并发编程的学习中，理解指令执行顺序是理解更复杂并发问题的基础。</p>
 * 
 * <p>注意：尽管代码本身没有涉及多线程，但在学习并发编程时，
 * 理解单线程环境下的执行顺序是理解多线程环境下可能出现的问题（如指令重排序、可见性问题等）的前提。</p>
 */
@Slf4j
public class CPUExecuteSequence {

    /**
     * 程序入口点，演示基本的指令执行顺序
     * 
     * <p>该方法按顺序执行以下操作：
     * <ol>
     *   <li>初始化变量a和b</li>
     *   <li>计算a和b的和</li>
     *   <li>记录并输出计算结果</li>
     * </ol>
     * 这展示了在单线程环境中指令按程序顺序执行的基本原理。</p>
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 初始化变量a和b可以随意调整顺序，只要放在求和之前即可。
        // 初始化第一个操作数
        int a = 1;
        
        // 初始化第二个操作数
        int b = 2;
        
        // 执行加法运算，计算两数之和
        int sum = a + b;
        
        // 记录并输出计算结果，格式为"a + b = sum"
        log.info("{} + {} = {}", a, b, sum);
    }

}