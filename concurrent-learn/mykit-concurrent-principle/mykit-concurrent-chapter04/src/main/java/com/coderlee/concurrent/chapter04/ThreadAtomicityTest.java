package com.coderlee.concurrent.chapter04;

import lombok.extern.slf4j.Slf4j;

/**
 * 线程原子性测试类
 * 
 * <p>该类用于演示多线程环境下的原子性问题。通过两个循环各启动1000个线程对同一个计数器进行递增操作，
 * 展示了非原子操作在并发环境下的线程安全问题。由于 count++ 操作不是原子性的，最终结果往往小于期望值2000。</p>
 * 
 * <p>存在的问题：
 * <ul>
 *   <li>{@link #count} 变量的递增操作（count++）不是原子性的</li>
 *   <li>多个线程同时访问和修改共享变量 {@link #count}，会导致数据竞争</li>
 *   <li>最终的计数值通常会小于预期值2000</li>
 * </ul>
 * </p>
 */
@Slf4j
public class ThreadAtomicityTest {
    
    /**
     * 计数器变量，用于演示非原子操作的线程安全问题
     * 
     * <p>该变量在多线程环境下被并发访问和修改，由于其递增操作不是原子性的，
     * 可能导致数据不一致的问题。</p>
     */
    private Long count = 0L;
    
    /**
     * 获取当前计数器的值
     * 
     * @return 当前计数器的值
     */
    public Long getCount() {
        return count;
    }

    /**
     * 对计数器进行递增操作
     * 
     * <p>该方法不是线程安全的，因为 count++ 操作实际上包含三个步骤：
     * <ol>
     *   <li>读取count的当前值</li>
     *   <li>将count的值加1</li>
     *   <li>将新值写回count变量</li>
     * </ol>
     * 在多线程环境下，这些步骤可能交错执行，导致数据丢失。</p>
     */
    public void incrementCount() {
        // count++ 不是原子操作，包含读取、计算、写入三个步骤
        count++;
    }

    /**
     * 主方法，用于测试多线程环境下的原子性问题
     * 
     * <p>启动2000个线程对计数器进行递增操作，理论上最终结果应该是2000，
     * 但由于{@link #incrementCount()}方法不是线程安全的，实际运行结果通常小于2000。</p>
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) { 
        // 创建测试实例
        ThreadAtomicityTest test = new ThreadAtomicityTest();
        
        // 启动第一轮1000个线程，每个线程执行一次计数器递增操作
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> test.incrementCount()).start();
        }
        
        // 启动第二轮1000个线程，每个线程执行一次计数器递增操作
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> test.incrementCount()).start();
        }
        
        // 输出最终的计数器值，由于线程调度的不确定性，结果通常小于2000
        log.info("count:{}", test.getCount());
    }
}