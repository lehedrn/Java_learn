package com.coderlee.designpattern.behavioral.observer.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 被观察者：订单服务
 * <p>
 * 管理观察者列表，订单状态变更时异步通知所有观察者
 * </p>
 *
 * @author coderlee
 */
public class OrderService {

    /**
     * 观察者列表
     */
    private final List<Observer> observers;

    /**
     * 线程池：用于异步执行通知任务
     */
    private final ExecutorService executor;

    public OrderService() {
        this.observers = new ArrayList<>();
        // 创建固定大小的线程池
        this.executor = Executors.newFixedThreadPool(4);
    }

    /**
     * 添加观察者（订阅）
     *
     * @param observer 观察者
     */
    public void attach(Observer observer) {
        observers.add(observer);
        System.out.println("✅ " + observer.getName() + " 已订阅订单通知");
    }

    /**
     * 移除观察者（取消订阅）
     *
     * @param observer 观察者
     */
    public void detach(Observer observer) {
        observers.remove(observer);
        System.out.println("❌ " + observer.getName() + " 已取消订阅订单通知");
    }

    /**
     * 更新订单状态，异步通知所有观察者
     * <p>
     * 使用 CompletableFuture 实现异步非阻塞通知
     * </p>
     *
     * @param orderId 订单 ID
     * @param status 订单状态
     * @param message 通知消息
     */
    public void updateOrderStatus(String orderId, String status, String message) {
        System.out.println("\n📢 订单 " + orderId + " 状态变更为：" + status);
        long startTime = System.currentTimeMillis();

        // 异步通知所有观察者
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (Observer observer : observers) {
            // 为每个观察者创建异步任务
            CompletableFuture<Void> future = CompletableFuture.runAsync(
                    () -> observer.update(orderId, status, message), executor);
            futures.add(future);
        }

        // 等待所有任务完成（非阻塞）
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long endTime = System.currentTimeMillis();
        System.out.println("⏱️ 异步通知完成，总耗时：" + (endTime - startTime) + "ms\n");
    }

    /**
     * 获取观察者数量
     *
     * @return 数量
     */
    public int getObserverCount() {
        return observers.size();
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        executor.shutdown();
    }
}
