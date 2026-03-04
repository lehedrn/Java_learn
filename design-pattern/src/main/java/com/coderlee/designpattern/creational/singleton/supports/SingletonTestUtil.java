package com.coderlee.designpattern.creational.singleton.supports;

import com.coderlee.designpattern.creational.singleton.SingletonExample01;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

/**
 * 单例模式通用测试工具类
 * <p>
 * 提供统一的测试方法来验证各种单例实现的正确性，包括：
 * 1. 基本单例性测试：验证多次获取是否返回同一实例
 * 2. 反射攻击测试：验证是否能抵抗反射破坏单例
 * 3. 序列化攻击测试：验证是否能抵抗序列化/反序列化破坏单例
 * 4. 克隆攻击测试：验证是否能抵抗 clone 方法破坏单例
 * </p>
 * <p>
 * 使用示例：
 * <pre>{@code
 * // 完整测试所有攻击方式
 * SingletonTestUtil.runFullTest(SingletonExample01.class, "饿汉式", SingletonExample01::getInstance);
 * }</pre>
 * </p>
 * 
 * @author coderlee
 * @version 1.0
 * @since 2026-03-04
 */
@Slf4j
public class SingletonTestUtil {

    /**
     * 测试单例的基本功能
     * <p>
     * 通过两次调用 supplier.get() 获取实例，验证返回的是否为同一个对象。
     * </p>
     *
     * @param singletonName 单例类的名称，用于日志输出
     * @param supplier 获取单例实例的 Supplier 函数式接口
     * @param <T> 泛型类型参数，表示单例对象的类型
     */
    public static<T> void testSingleton(String singletonName, Supplier<T> supplier) {
        log.info("========== 验证 {} 的单例性 ==========", singletonName);

        // 第一次调用 supplier.get() 获取单例实例
        T instance1 = supplier.get();
        
        // 第二次调用 supplier.get() 获取单例实例
        T instance2 = supplier.get();

        // 使用 == 比较两个实例的内存地址是否相同
        log.info("instance1 {} instance2", instance1 == instance2 ? "==" : "!=");
        log.info("instance1 hashcode: {} ", instance1.hashCode());
        log.info("instance2 hashcode: {} ", instance2.hashCode());

        if (instance1 == instance2) {
            log.info("✅ {} 单例验证通过！", singletonName);
        } else {
            log.error("❌ {} 单例验证失败！", singletonName);
        }
    }

    /**
     * 测试使用反射机制破坏单例模式
     * <p>
     * 通过反射 API 绕过私有构造函数的访问限制，尝试创建新的实例。
     * </p>
     *
     * @param singletonName 单例类的名称，用于日志输出
     * @param singletonClass 单例类的 Class 对象，用于获取构造函数
     * @param supplier 获取单例实例的 Supplier 函数式接口
     * @param <T> 泛型类型参数，表示单例对象的类型
     */
    public static<T> void testReflection(String singletonName, Class<T> singletonClass, Supplier<T> supplier) {
        log.info("========== 使用反射破坏 {} ==========", singletonName);

        try {
            // 获取私有的构造函数
            Constructor<T> declaredConstructor = null;
            if (supplier.get().getClass().isEnum()) {
                declaredConstructor = singletonClass.getDeclaredConstructor(String.class, int.class);
            } else {
                declaredConstructor = singletonClass.getDeclaredConstructor();
            }
            // 暴力反射：设置私有构造函数可访问
            declaredConstructor.setAccessible(true);
            // 通过反射创建新实例
            T instance = declaredConstructor.newInstance();

            // 获取正常单例实例进行对比
            T normalInstance = supplier.get();
            
            log.info("反射实例 {} 正常实例", instance == normalInstance ? "==" : "!=");
            log.info("反射实例 hashcode: {} ", instance.hashCode());
            log.info("正常实例 hashcode: {}", normalInstance.hashCode());

            if (instance != normalInstance) {
                log.warn("⚠️  使用反射会破坏 {} 单例模式！", singletonName);
            } else {
                log.info("✅ {} 能够抵抗反射攻击！", singletonName);
            }
        } catch (Exception e) {
            log.error("反射创建实例失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试序列化/反序列化破坏单例模式
     * <p>
     * 如果单例类实现了 Serializable 接口，反序列化时会创建新的实例。
     * </p>
     *
     * @param singletonName 单例类的名称，用于日志输出
     * @param supplier 获取单例实例的 Supplier 函数式接口
     * @param <T> 泛型类型参数，必须是 Serializable 类型
     */
    public static<T extends Serializable> void testSerialization(String singletonName, Supplier<T> supplier) {
        log.info("========== 使用序列化破坏 {} ==========", singletonName);

        try {
            // 获取原始单例实例
            T instance1 = supplier.get();
            
            // 序列化到字节数组
            ByteArrayInputStream bais;
            try (
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ) {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(instance1);

                // 从字节数组反序列化
                bais = new ByteArrayInputStream(baos.toByteArray());
            }
            T instance2;
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                instance2 = (T) ois.readObject();
            }

            // 验证是否是同一个实例
            log.info("序列化前实例 {} 序列化后实例", instance1 == instance2 ? "==" : "!=");
            log.info("序列化前 hashcode: {} ", instance1.hashCode());
            log.info("序列化后 hashcode: {}", instance2.hashCode());
            
            if (instance1 != instance2) {
                log.warn("⚠️  序列化会破坏 {} 单例模式！", singletonName);
            } else {
                log.info("✅ {} 能抵抗序列化攻击！", singletonName);
            }
        } catch (NotSerializableException e) {
            log.info("ℹ️  {} 未实现 Serializable 接口，跳过序列化测试", singletonName);
        } catch (Exception e) {
            log.error("序列化测试失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试克隆方法破坏单例模式
     * <p>
     * 如果单例类实现了 Cloneable 接口，clone() 方法可能创建新实例。
     * </p>
     *
     * @param singletonName 单例类的名称，用于日志输出
     * @param supplier 获取单例实例的 Supplier 函数式接口
     * @param <T> 泛型类型参数
     */
    @SuppressWarnings("unchecked")
    public static<T> void testClone(String singletonName, Supplier<T> supplier) {
        log.info("========== 使用克隆破坏 {} ==========", singletonName);

        try {
            T instance1 = supplier.get();
            
            // 检查是否实现了 Cloneable 接口
            if (!(instance1 instanceof Cloneable)) {
                log.info("ℹ️  {} 未实现 Cloneable 接口，跳过克隆测试", singletonName);
                return;
            }
            
            // 通过反射调用 clone 方法
            Method cloneMethod = instance1.getClass().getDeclaredMethod("clone");
            cloneMethod.setAccessible(true);
            T instance2 = (T) cloneMethod.invoke(instance1);
            
            log.info("原始实例 {} 克隆实例", instance1 == instance2 ? "==" : "!=");
            log.info("原始实例 hashcode: {} ", instance1.hashCode());
            log.info("克隆实例 hashcode: {}", instance2.hashCode());
            
            if (instance1 != instance2) {
                log.warn("⚠️  clone 会破坏 {} 单例模式！", singletonName);
            } else {
                log.info("✅ {} 能抵抗克隆攻击！", singletonName);
            }
        } catch (NoSuchMethodException e) {
            log.info("ℹ️  {} 没有重写 clone() 方法，跳过克隆测试", singletonName);
        } catch (Exception e) {
            log.error("克隆测试失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 测试单例模式的线程安全性
     * <p>
     * 通过创建大量并发线程同时调用 getInstance() 方法，
     * 验证是否会创建多个实例。
     * </p>
     *
     * @param singletonName 单例类的名称，用于日志输出
     * @param supplier 获取单例实例的 Supplier 函数式接口
     * @param <T> 泛型类型参数，表示单例对象的类型
     */
    public static<T> void testSingletonThreadSafety(String singletonName, Supplier<T> supplier) {
        log.info("========== 测试 {} 的线程安全性 ==========", singletonName);
        
        try {
            // 使用 ConcurrentHashMap.newKeySet() 保证线程安全的集合操作
            Set<T> instances = ConcurrentHashMap.newKeySet();
            
            // 定义线程数量：10000 个线程足够验证并发问题
            // 1. 系统资源耗尽
            // 2. 测试执行缓慢
            // 3. 线程调度开销大
            final int THREAD_COUNT = 1000;
            
            // CountDownLatch 用于同步等待所有线程执行完成
            CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
            
            log.info("启动 {} 个线程并发测试...", THREAD_COUNT);
            
            // 创建并启动线程
            for (int i = 0; i < THREAD_COUNT; i++) {
                new Thread(() -> {
                    try {
                        // 获取单例实例并添加到集合中（自动去重）
                        T instance = supplier.get();
                        instances.add(instance);
                    } finally {
                        // 确保计数，即使发生异常
                        latch.countDown();
                    }
                }).start();
            }
            
            // 等待所有线程执行完成
            latch.await();
            
            // 统计创建的实例数量
            int uniqueInstanceCount = instances.size();
            
            log.info("实际创建的唯一定义数量：{}", uniqueInstanceCount);
            
            // 判断是否线程安全
            if (uniqueInstanceCount != 1) {
                log.warn("⚠️  单例类 {} 不是线程安全的！创建了 {} 个不同实例", 
                    singletonName, uniqueInstanceCount);
            } else {
                log.info("✅ {} 线程安全！所有线程都获取到同一个实例", singletonName);
            }
            
        } catch (InterruptedException e) {
            // 恢复中断状态
            Thread.currentThread().interrupt();
            log.error("❌ 线程安全测试被中断：{}", e.getMessage());
            throw new RuntimeException("线程安全测试失败", e);
        } catch (Exception e) {
            log.error("❌ 线程安全测试异常：{}", e.getMessage(), e);
            throw new RuntimeException("线程安全测试失败", e);
        }
    }

    public static<T> void testThreadSingleton(String singletonName, Supplier<T> supplier) {
        log.info("========== 验证 {} 的线程单例性 ==========", singletonName);
        int threadCount = 5;
        Set<T> instances = ConcurrentHashMap.newKeySet();
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                T instance = supplier.get();
                instances.add(instance);
                log.info("{} -> {}", Thread.currentThread().getName(), instance.hashCode());
                latch.countDown();
            }, "t" + (i+1)).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("线程中断：{}", e.getMessage());
        }
        if (instances.size() == threadCount) {
            log.info(" {} 是线程间单例！", singletonName);
        } else if (instances.size() == 1) {
            log.info(" {} 是进程内单例！", singletonName);
        }
    }

}
