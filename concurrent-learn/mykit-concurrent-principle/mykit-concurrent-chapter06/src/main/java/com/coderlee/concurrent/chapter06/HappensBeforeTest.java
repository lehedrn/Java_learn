package com.coderlee.concurrent.chapter06;

import lombok.extern.slf4j.Slf4j;

/**
 * 演示Java内存模型中happens-before原则的测试类
 * <p>
 * 该类通过不同的方法展示了happens-before的八大原则：
 * 1. 程序次序规则
 * 2. volatile变量规则
 * 3. 锁定规则
 * 4. 线程启动规则
 * 5. 线程终止规则
 * 6. 线程中断规则
 * 7. 对象终结规则
 * 8. 传递规则比较简单，不做演示
 * </p>
 * 
 * @author coderlee
 */
@Slf4j
public class HappensBeforeTest {
    
    /**
     * 构造函数，初始化测试类并记录日志
     */
    public HappensBeforeTest() {
        log.info("HappensBeforeTest init");
    }

    /**
     * 对象被垃圾回收时调用的finalize方法
     * 演示对象终结规则
     * 
     * @throws Throwable 可能抛出的异常
     */
    @Override
    protected void finalize() throws Throwable {
        log.info("HappensBeforeTest finalize");
        super.finalize();
    }

    /**
     * 主方法，创建对象实例并触发垃圾回收
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        HappensBeforeTest hb = new HappensBeforeTest();
        // 对象终结规则
        /* // 创建对象实例
        // 建议JVM进行垃圾回收，触发finalize方法
        System.gc(); */
        // 程序次序原则
        /* hb.programOrder();
        hb.programOrder1(); */
        // volatile变量规则
        /* // volatile变量写规则
        new Thread(hb::writeAmountAndCount).start();
        // volatile变量读规则
        new Thread(hb::readAmountAndCount).start(); */
        // 锁定规则
        /* Thread t1 = new Thread(hb::synchrionizedUpdateValue);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("value is {}", hb.value); */
        // 线程启动规则
        /* hb.threadStart(); */
        // 线程终结规则
        /* try {
            hb.threadEnd();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } */
        // 线程中断原则
        try {
            hb.threadInterrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 程序次序原则（Program Order Rule）
     * <p>
     * 在同一个线程中，按照程序代码顺序，前面的操作happens-before于后面的任意操作
     * </p>
     */
    public void programOrder(){
        // 按顺序执行变量赋值和计算操作
        int a = 1;          // 操作1
        int b = 2;          // 操作2
        int sum = a + b;    // 操作3，依赖于操作1和操作2
        log.info("sum: {}", sum);
    }
    public void programOrder1(){
        // 按顺序执行变量赋值和计算操作
        int b = 2;          // 操作2
        int a = 1;          // 操作1
        int sum = a + b;    // 操作3，依赖于操作1和操作2
        log.info("sum: {}", sum);
    }


    // volatile变量，用于演示volatile变量规则
    private volatile int count = 0;
    // 普通变量，与volatile变量配合演示volatile规则
    private double amount = 0;

    /**
     * volatile变量写规则（Volatile Variable Write Rule）
     * <p>
     * 对一个volatile变量的写操作happens-before于后面对该变量的读操作
     * </p>
     */
    public void writeAmountAndCount(){
        // 先写入普通变量amount
        amount = 1;
        // 后写入volatile变量count，保证对amount的写入happens-before于其他线程对count的读取
        count = 1;
    }

    /**
     * volatile变量读规则（Volatile Variable Read Rule）
     * <p>
     * 对一个volatile变量的读操作能够看到对该变量之前的写操作结果
     * </p>
     */
    public void readAmountAndCount(){
        // 读取volatile变量count
        if (count == 1){
            // 由于volatile的happens-before关系，这里能保证看到amount的最新值
            log.info("amount: {}", amount);
        }
    }


    // 普通变量，用于演示锁定规则
    private int value = 0;
    
    /**
     * 锁定原则（Monitor Lock Rule）
     * <p>
     * 对一个锁的解锁happens-before于随后对这个锁的加锁
     * </p>
     */
    public void synchrionizedUpdateValue(){
        // 使用synchronized关键字获取对象锁
        synchronized (this){
            // 在同步块内访问和修改共享变量
            if (value < 1){
                value = 1;
            }
        }
        // synchronized块结束时释放锁
    }


    /**
     * 线程启动原则（Thread Start Rule）
     * <p>
     * Thread对象的start()方法happens-before于此线程的每一个动作
     * </p>
     */
    public void threadStart(){
        // 创建新线程
        Thread thread2 = new Thread(()-> {
            // 子线程中读取value值
            log.info("value: {}", value);
        });
        // 在启动线程前修改value值
        value = 10;
        // 启动线程，保证对value的修改happens-before于子线程中的读取
        thread2.start();
    }

    /**
     * 线程终结原则（Thread Join Rule）
     * <p>
     * 线程中的所有操作都happens-before于对此线程的join()方法返回
     * </p>
     * 
     * @throws InterruptedException 当线程等待过程中被中断时抛出
     */
    public void threadEnd() throws InterruptedException {
        // 创建新线程用于修改value值
        Thread thread2 = new Thread(()-> {
            // 在子线程中修改value值
            value = 10;
        });
        // 启动子线程
        thread2.start();
        // 等待子线程执行完毕，确保能看到子线程对value的修改
        thread2.join();
        // 此处可以安全地读取到子线程修改后的value值
        log.info("value: {}", value);
    }


    /**
     * 线程中断原则（Thread Interruption Rule）
     * <p>
     * 对线程interrupt()方法的调用happens-before于被中断线程的代码检测到中断事件的发生
     * </p>
     * 
     * @throws Exception 可能抛出的异常
     */
    public void threadInterrupt() throws Exception{
        // 创建新线程处理中断事件
        Thread thread2 = new Thread(()->{
            // 检查当前线程是否被中断
            if(Thread.currentThread().isInterrupted()){
                // 如果被中断，则输出value值
                log.info("value: {}", value);
            }
        });
        // 启动线程
        thread2.start();
        // 在中断线程前修改value值
        value = 10;
        // 中断线程，保证value的修改happens-before于中断处理代码中的读取
        thread2.interrupt();
    }
}