package com.coderlee.concurrent.chapter06;

import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * volatile原子性测试类
 * 
 * <p>该类用于演示volatile关键字无法保证复合操作的原子性。
 * 虽然{@link #count}变量被声明为volatile，但对其执行的递增操作(count++)并不是原子性的，
 * 因此在多线程环境下仍然会出现数据竞争和结果不一致的问题。</p>
 * 
 * <p>该测试通过两个线程各执行1000次递增操作来验证这一点。
 * 尽管使用了volatile关键字，最终结果通常会小于期望值2000，
 * 这证明了volatile只能保证可见性和有序性，而不能保证操作的原子性。</p>
 * 
 * <p>关键知识点：
 * <ul>
 *   <li>volatile关键字的作用：保证可见性和禁止指令重排序</li>
 *   <li>volatile的局限性：无法保证复合操作的原子性</li>
 *   <li>count++操作的非原子性：包含读取、计算、写入三个步骤</li>
 * </ul>
 * </p>
 * 
 * @see VolatileVisibilityTest
 * @see VolatileOrderingTest
 */
@Slf4j
public class VolatileAtomicityTest {
    
    /**
     * 使用volatile修饰的计数器变量
     * 
     * <p>volatile关键字确保了：
     * <ul>
     *   <li>可见性：一个线程对[count]的修改对其他线程立即可见</li>
     *   <li>有序性：禁止指令重排序，插入内存屏障</li>
     * </ul>
     * </p>
     * 
     * <p>但是，volatile无法保证count++操作的原子性，该操作实际上包含三个步骤：
     * 1. 读取[count]的当前值；2. 将[count]加1；3. 将新值写回[count]变量。</p>
     */
    private volatile Long count = 0L;

    /**
     * 对计数器进行递增操作
     * 
     * <p>尽管[count]变量被声明为volatile，但count++操作本身不是原子性的。
     * 多个线程可能同时读取到相同的[count]值，然后各自加1并写回，导致本应多次的递增操作只生效一次。</p>
     * 
     * <p>要保证原子性，可以考虑以下解决方案：
     * <ul>
     *   <li>使用synchronized关键字同步该方法</li>
     *   <li>使用AtomicLong替代volatile Long</li>
     *   <li>使用显式锁如ReentrantLock</li>
     * </ul>
     * </p>
     */
    public void incrementCount() {
        // count++操作不是原子性的，即使count被声明为volatile
        // 该操作包含三个步骤：读取->计算->写入，可能被其他线程中断
        count++;
    }

    /**
     * 执行多线程原子性测试
     * 
     * <p>该方法创建两个线程，每个线程使用{@link IntStream}对计数器执行1000次递增操作，
     * 总共应该执行2000次递增操作。通过{@link Thread#join()}方法确保主线程等待两个子线程执行完成，
     * 然后返回最终的计数值。</p>
     * 
     * @return 执行完多线程操作后的计数器最终值
     * @throws InterruptedException 当线程等待过程中被中断时抛出
     */
    public Long execute() throws InterruptedException {
        // 创建第一个线程，执行1000次递增操作
        Thread thread1 = new Thread(() -> {
            IntStream.range(0, 1000).forEach((i) -> incrementCount());
        });

        // 创建第二个线程，执行1000次递增操作
        Thread thread2 = new Thread(() -> {
            IntStream.range(0, 1000).forEach((i) -> incrementCount());
        });

        // 启动第一个线程
        thread1.start();
        
        // 启动第二个线程
        thread2.start();

        // 等待第一个线程执行完成
        thread1.join();
        
        // 等待第二个线程执行完成
        thread2.join();

        // 返回最终的计数器值
        return count;
    }

    /**
     * 主方法，用于测试volatile关键字在原子性方面的局限性
     * 
     * <p>创建{@link VolatileAtomicityTest}实例并执行测试，理论上最终结果应该是2000，
     * 但由于count++操作不是原子性的，即使使用了volatile关键字，
     * 实际运行结果通常小于2000，这证明了volatile无法保证复合操作的原子性。</p>
     * 
     * @param args 命令行参数
     * @throws InterruptedException 当线程等待过程中被中断时抛出
     */
    public static void main(String[] args) throws InterruptedException {
        // 创建测试实例
        VolatileAtomicityTest multiThreadAtomicity = new VolatileAtomicityTest();
        
        // 执行多线程测试并获取结果
        Long count = multiThreadAtomicity.execute();
        
        // 输出结果和比较结果，通常会发现count小于2000，说明volatile无法保证原子性
        log.info("count:{}, count == 2000 is {}", count, count == 2000);
    }
}