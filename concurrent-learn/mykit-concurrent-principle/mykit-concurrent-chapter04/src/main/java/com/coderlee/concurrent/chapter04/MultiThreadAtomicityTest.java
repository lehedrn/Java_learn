package com.coderlee.concurrent.chapter04;

import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * 多线程原子性测试类
 * 
 * <p>该类用于演示多线程环境下的原子性问题。通过创建两个线程，每个线程对共享计数器执行100次递增操作，
 * 展示了非原子操作在并发环境下的线程安全问题。由于 count++ 操作不是原子性的，最终结果往往小于期望值200。</p>
 * 
 * <p>该测试比 {@link ThreadAtomicityTest} 更加结构化，使用了明确的线程管理和同步机制，
 * 能够更准确地演示原子性问题。</p>
 * 
 * <p>存在的问题：
 * <ul>
 *   <li>{@link #count} 变量的递增操作（count++）不是原子性的</li>
 *   <li>多个线程同时访问和修改共享变量 {@link #count}，会导致数据竞争</li>
 *   <li>最终的计数值通常会小于预期值200</li>
 * </ul>
 * </p>
 */
@Slf4j
public class MultiThreadAtomicityTest {

    /**
     * 计数器变量，用于演示非原子操作的线程安全问题
     * 
     * <p>该变量在多线程环境下被并发访问和修改，由于其递增操作不是原子性的，
     * 可能导致数据不一致的问题。初始值为0L。</p>
     */
    private Long count = 0L;

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
     * 执行多线程原子性测试
     * 
     * <p>该方法创建两个线程，每个线程使用 {@link IntStream} 对计数器执行100次递增操作，
     * 总共应该执行200次递增操作。通过 {@link Thread#join()} 方法确保主线程等待两个子线程执行完成，
     * 然后返回最终的计数值。</p>
     * 
     * @return 执行完多线程操作后的计数器最终值
     */
    public Long execute() {
        // 创建第一个线程，执行100次递增操作
        Thread thread1 = new Thread(() -> IntStream.range(0, 100).forEach(i -> incrementCount()));
        
        // 创建第二个线程，执行100次递增操作
        Thread thread2 = new Thread(() -> IntStream.range(0, 100).forEach(i -> incrementCount()));

        // 启动第一个线程
        thread1.start();
        
        // 启动第二个线程
        thread2.start();

        try {
            // 等待第一个线程执行完成
            thread1.join();
            
            // 等待第二个线程执行完成
            thread2.join();
        } catch (InterruptedException e) {
            // 处理线程中断异常
            e.printStackTrace();
        }

        // 返回最终的计数器值
        return count;
    }

    /**
     * 主方法，用于测试多线程环境下的原子性问题
     * 
     * <p>创建 {@link MultiThreadAtomicityTest} 实例并执行测试，理论上最终结果应该是200，
     * 但由于{@link #incrementCount()}方法不是线程安全的，实际运行结果通常小于200，
     * 这证明了 count++ 操作不是原子性的。</p>
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建测试实例
        MultiThreadAtomicityTest test = new MultiThreadAtomicityTest();
        
        // 执行多线程测试并获取结果
        Long result = test.execute();
        
        // 输出结果，通常会发现结果小于200（例如198），说明了++操作不是原子操作
        log.info("Final count value: {}", result);
    }
}