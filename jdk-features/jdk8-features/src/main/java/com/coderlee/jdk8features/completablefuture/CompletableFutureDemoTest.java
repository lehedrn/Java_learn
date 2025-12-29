package com.coderlee.jdk8features.completablefuture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class CompletableFutureDemoTest {

    private static final ExecutorService IO_EXECUTOR =
            Executors.newFixedThreadPool(4);

    private static final Random RANDOM = new Random();

    /* =========================
       模拟 RPC / IO 调用
       ========================= */

    private User queryUser(Long userId) {
        sleep(300);
        return new User(userId, "Lee");
    }

    private List<Order> queryOrders(Long userId) {
        sleep(400);
        return IntStream.range(0, 3)
                .mapToObj(i -> new Order("order-" + i))
                .collect(Collectors.toList());
    }

    private Integer queryPoints(Long userId) {
        sleep(200);
        return RANDOM.nextInt(1000);
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    /* =========================
       1️⃣ Future 版本
       ========================= */

    @Test
    public void testFutureStyle() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        long start = System.currentTimeMillis();

        Future<User> userFuture =
                executor.submit(() -> queryUser(1L));

        Future<List<Order>> orderFuture =
                executor.submit(() -> queryOrders(1L));

        Future<Integer> pointFuture =
                executor.submit(() -> queryPoints(1L));

        // ❌ 阻塞等待，顺序 get
        User user = userFuture.get();
        List<Order> orders = orderFuture.get();
        Integer points = pointFuture.get();

        UserProfile profile =
                new UserProfile(user, orders, points);

        log.info("Future result: {}", profile);
        log.info("Future cost: {} ms",
                System.currentTimeMillis() - start);

        executor.shutdown();
    }

    /*
     * 问题总结：
     * 1. 必须显式 get()
     * 2. 阻塞点分散
     * 3. 无法声明“完成后做什么”
     * 4. 异常处理极不优雅
     */

    /* =========================
       2️⃣ CompletableFuture 基础用法
       ========================= */

    @Test
    public void testCompletableFutureBasic() {
        long start = System.currentTimeMillis();

        CompletableFuture<User> userFuture =
                CompletableFuture.supplyAsync(
                        () -> queryUser(1L), IO_EXECUTOR);

        CompletableFuture<List<Order>> orderFuture =
                CompletableFuture.supplyAsync(
                        () -> queryOrders(1L), IO_EXECUTOR);

        CompletableFuture<Integer> pointFuture =
                CompletableFuture.supplyAsync(
                        () -> queryPoints(1L), IO_EXECUTOR);

        CompletableFuture<UserProfile> profileFuture =
                userFuture.thenCombine(orderFuture,
                                (user, orders) ->
                                        new UserProfile(user, orders, null))
                        .thenCombine(pointFuture,
                                (profile, points) -> {
                                    profile.setPoints(points);
                                    return profile;
                                });

        UserProfile profile = profileFuture.join();

        log.info("CompletableFuture result: {}", profile);
        log.info("CompletableFuture cost: {} ms",
                System.currentTimeMillis() - start);
    }

    /* =========================
       3️⃣ thenCompose：异步依赖
       ========================= */

    @Test
    public void testThenCompose() {
        CompletableFuture<UserProfile> future =
                CompletableFuture
                        .supplyAsync(() -> queryUser(1L), IO_EXECUTOR)
                        .thenCompose(user ->
                                CompletableFuture.supplyAsync(
                                                () -> queryOrders(user.getId()),
                                                IO_EXECUTOR)
                                        .thenApply(orders ->
                                                new UserProfile(user, orders, null))
                        );

        log.info("thenCompose result: {}", future.join());
    }

    /*
     * 说明：
     * thenCompose = 异步 flatMap
     * 用于“后一个任务依赖前一个结果”的场景
     */

    /* =========================
       4️⃣ allOf：批量并发编排
       ========================= */

    @Test
    public void testAllOf() {
        CompletableFuture<User> userFuture =
                CompletableFuture.supplyAsync(
                        () -> queryUser(1L), IO_EXECUTOR);

        CompletableFuture<List<Order>> orderFuture =
                CompletableFuture.supplyAsync(
                        () -> queryOrders(1L), IO_EXECUTOR);

        CompletableFuture<Integer> pointFuture =
                CompletableFuture.supplyAsync(
                        () -> queryPoints(1L), IO_EXECUTOR);

        CompletableFuture<Void> all =
                CompletableFuture.allOf(
                        userFuture, orderFuture, pointFuture);

        UserProfile profile = all.thenApply(v ->
                        new UserProfile(
                                userFuture.join(),
                                orderFuture.join(),
                                pointFuture.join()))
                .join();

        log.info("allOf result: {}", profile);
    }

    /* =========================
       5️⃣ 异常处理示例
       ========================= */

    @Test
    public void testExceptionHandle() {
        CompletableFuture<Integer> future =
                CompletableFuture.supplyAsync(() -> {
                            if (true) {
                                throw new RuntimeException("RPC error");
                            }
                            return 1;
                        }, IO_EXECUTOR)
                        .exceptionally(ex -> {
                            log.error("error occurred", ex);
                            return -1;
                        });

        log.info("exception result: {}", future.join());
    }

    /* =========================
       模型类
       ========================= */

    @Data
    @AllArgsConstructor
    static class User {
        private Long id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class Order {
        private String orderId;
    }

    @Data
    @AllArgsConstructor
    static class UserProfile {
        private User user;
        private List<Order> orders;
        private Integer points;
    }
}
