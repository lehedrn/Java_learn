package com.coderlee.concurrent.design.active.object.right;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 代理请求存储实现
 * <p>
 * 该类作为Active Object模式中的Proxy角色，将方法调用转换为异步执行，
 * 通过线程池管理并发执行，避免阻塞调用线程
 * </p>
 */
@Slf4j
public class ProxyRequestStore implements RequestStore {

    /**
     * 单例实例
     * <p>
     * 采用饿汉式单例模式保证全局唯一实例
     * </p>
     */
    private static final RequestStore INSTANCE = new ProxyRequestStore();

    /**
     * 获取单例实例
     *
     * @return ProxyRequestStore单例实例
     */
    public static RequestStore getInstance() {
        return INSTANCE;
    }

    /**
     * 私有构造函数
     * <p>
     * 防止外部直接实例化
     * </p>
     */
    private ProxyRequestStore() {
    }

    /**
     * 线程池执行器
     * <p>
     * 用于异步执行存储请求，控制并发数量和队列大小
     * </p>
     */
    private final ThreadPoolExecutor scheduler = new ThreadPoolExecutor(
            3,                            // 核心线程数
            3,                            // 最大线程数
            1,                            // 空闲线程存活时间
            TimeUnit.HOURS,              // 时间单位
            new ArrayBlockingQueue<>(256), // 有界阻塞队列
            r -> new Thread(r, "scheduler-thread"), // 线程工厂
            new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略

    /**
     * 实际的存储服务实例
     * <p>
     * 采用组合模式，将具体存储操作委托给实际实现类
     * </p>
     */
    private final RequestStore requestStore = new DBRequestStore();

    /**
     * 异步执行商品请求存储
     * <p>
     * 将存储请求封装为Callable任务提交到线程池异步执行，
     * 避免阻塞调用线程
     * </p>
     *
     * @param goodsRequest 商品请求对象
     * @see RequestStore#flush(GoodsRequest)
     * @see DBRequestStore
     */
    @Override
    public void flush(GoodsRequest goodsRequest) {
        // 将存储请求封装为可执行任务
        Callable<Boolean> methodRRequest = () -> {
            // 记录任务开始执行
            log.info("调度执行缓冲区中的MethodRequest请求");

            // 委托给实际存储服务执行
            requestStore.flush(goodsRequest);

            // 返回执行结果标识
            return Boolean.TRUE;
        };

        // 记录任务提交到队列
        log.info("将MethodRequest请求放入缓冲区");

        // 提交任务到线程池执行
        scheduler.submit(methodRRequest);
    }

    /**
     * 关闭存储服务资源
     * <p>
     * 关闭线程池，释放相关资源
     * </p>
     *
     * @throws IOException IO操作异常
     */
    @Override
    public void close() throws IOException {
        // 关闭线程池，不再接受新任务
        scheduler.shutdown();
    }
}
