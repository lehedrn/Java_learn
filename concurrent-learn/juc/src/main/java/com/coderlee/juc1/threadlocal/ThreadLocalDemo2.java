package com.coderlee.juc1.threadlocal;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadLocal 在线程池中的使用示例
 * <p>
 * 演示了在线程池环境中使用 ThreadLocal 时是否清理资源的不同效果
 * <p>
 * 【强制】必须回收自定义的 ThreadLocal 变量，尤其在线程池场景下，线程经常会被复用，如果不清理自定义的 ThreadLocal 变量，可能会影响后续业务逻辑和造成内存泄露等问题。尽量在代理中使用try-finally 块进行回收。
 *
 */
@Slf4j
public class ThreadLocalDemo2 {
    public static void main(String[] args) {
        // 先运行不清理 ThreadLocal 的情况
        runThreadLocal(false);
        // 再运行清理 ThreadLocal 的情况
        runThreadLocal(true);
    }

    /**
     * 运行 ThreadLocal 测试
     *
     * @param isRemove 是否在使用完 ThreadLocal 后进行清理
     */
    public static void runThreadLocal(boolean isRemove) {
        log.info("是否用完就清空ThreadLocal -----------> {}", isRemove);
        // 创建共享的数据对象
        MyData myData = new MyData();
        // 创建固定大小为3的线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try {
            // 提交10个任务到线程池执行
            for (int i = 0; i < 10; i++) {
                threadPool.submit(() -> {
                    try {
                        // 获取操作前的值
                        Integer beforeInt = myData.threadLocalFiled.get();
                        // 执行加1操作
                        myData.threadLocalFiled.set(1 + myData.threadLocalFiled.get());
                        // 获取操作后的值
                        Integer afterInt = myData.threadLocalFiled.get();
                        // 记录线程名和操作前后值的变化
                        log.info("threadName: {} ---------------------> before: {}, after: {}",
                                Thread.currentThread().getName(), beforeInt, afterInt);
                    } finally {
                        // 根据参数决定是否清理 ThreadLocal 资源
                        if (isRemove) {
                            myData.threadLocalFiled.remove();
                        }
                    }
                });
            }
        } finally {
            // 关闭线程池
            threadPool.shutdown();
        }
    }
}

/**
 * 数据封装类，使用 ThreadLocal 维护线程本地数据
 */
class MyData {
    // 定义 ThreadLocal 变量，初始值为0
    static final ThreadLocal<Integer> threadLocalFiled = ThreadLocal.withInitial(() -> 0);
}
