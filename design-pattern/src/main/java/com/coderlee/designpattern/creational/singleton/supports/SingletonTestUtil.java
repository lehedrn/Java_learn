package com.coderlee.designpattern.creational.singleton.supports;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

/**
 * 单例模式通用测试工具类
 * <p>
 * 提供统一的测试方法来验证各种单例实现的正确性，包括：
 * 1. 基本单例性测试：验证多次获取是否返回同一实例
 * 2. 反射攻击测试：验证是否能抵抗反射破坏单例
 * </p>
 * <p>
 * 使用示例：
 * <pre>{@code
 * // 测试饿汉式单例
 * SingletonTestUtil.testSingleton("饿汉式单例", SingletonExample01::getInstance);
 * SingletonTestUtil.testReflection("饿汉式单例", SingletonExample01.class, SingletonExample01::getInstance);
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
     * 这是验证单例模式最基本的方法。
     * </p>
     *
     * @param singletonName 单例类的名称，用于日志输出
     * @param supplier 获取单例实例的 Supplier 函数式接口，通常传入 {@code ClassName::getInstance} 方法引用
     * @param <T> 泛型类型参数，表示单例对象的类型
     * 
     * @see Supplier#get()
     */
    public static<T> void testSingleton(String singletonName, Supplier<T> supplier) {
        log.info("========== 验证 {} 的单例性 ==========", singletonName);

        // 第一次调用 supplier.get() 获取单例实例
        // 实际执行：SingletonExampleXX.getInstance()
        T instance1 = supplier.get();
        
        // 第二次调用 supplier.get() 获取单例实例
        // 注意：这里是重新调用方法，不是缓存结果
        T instance2 = supplier.get();

        // 使用 == 比较两个实例的内存地址是否相同
        // 如果是单例，应该返回 true（同一个对象引用）
        log.info("instance1 {} instance2", instance1 == instance2 ? "==" : "!=");
        
        // 输出第一个实例的哈希码（基于内存地址计算）
        log.info("instance1 hashcode: {} ", instance1.hashCode());
        
        // 输出第二个实例的哈希码
        // 如果是单例，两个 hashcode 应该完全相同
        log.info("instance2 hashcode: {} ", instance2.hashCode());

        // 根据比较结果输出验证结论
        if (instance1 == instance2) {
            // ✅ 两次获取的是同一个对象，单例验证通过
            log.info("✅ {} 单例验证通过！", singletonName);
        } else {
            // ❌ 两次获取的是不同对象，单例验证失败
            log.error("❌ {} 单例验证失败！", singletonName);
        }
    }

    /**
     * 测试使用反射机制破坏单例模式
     * <p>
     * 通过反射 API 绕过私有构造函数的访问限制，尝试创建新的实例，
     * 验证单例模式是否能够抵抗反射攻击。
     * </p>
     * <p>
     * 注意：饿汉式和懒汉式单例都无法抵抗反射攻击，
     * 只有枚举实现或带防御代码的单例才能防止。
     * </p>
     *
     * @param singletonName 单例类的名称，用于日志输出
     * @param singletonClass 单例类的 Class 对象，用于获取构造函数
     * @param supplier 获取单例实例的 Supplier 函数式接口，用于获取正常实例进行对比
     * @param <T> 泛型类型参数，表示单例对象的类型
     * 
     * @throws RuntimeException 当反射操作失败时抛出
     * @see Constructor#setAccessible(boolean)
     * @see Class#getDeclaredConstructor()
     */
    public static<T> void testReflection(String singletonName, Class<T> singletonClass, Supplier<T> supplier) {
        log.info("========== 使用反射破坏 {} ==========", singletonName);

        try {
            // 步骤 1：获取私有的构造函数对象
            // getDeclaredConstructor() 可以获取类的所有构造函数，包括私有的
            Constructor<T> declaredConstructor = singletonClass.getDeclaredConstructor();
            
            // 步骤 2：暴力反射 - 绕过 Java 的访问控制检查
            // setAccessible(true) 允许访问私有成员，这是破坏单例的关键
            declaredConstructor.setAccessible(true);
            
            // 步骤 3：通过反射调用构造函数创建新实例
            // 这会创建一个全新的对象，与单例实例不同
            T instance = declaredConstructor.newInstance();

            // 步骤 4：通过正常方式获取单例实例，用于对比
            T normalInstance = supplier.get();
            
            // 步骤 5：比较反射创建的实例与正常获取的实例是否为同一个对象
            log.info("instance {} instance2", instance == normalInstance ? "==" : "!=");
            
            // 输出反射实例的哈希码
            log.info("instance hashcode: {} ", instance.hashCode());
            
            // 输出正常实例的哈希码
            log.info("normalInstance hashcode: {}", normalInstance.hashCode());

            // 根据比较结果判断单例是否被破坏
            if (instance != normalInstance) {
                // ⚠️ 反射创建了不同的实例，说明单例模式被破坏
                log.warn("⚠️  使用反射会破坏 {} 单例模式！", singletonName);
            } else {
                // ✅ 反射创建的也是同一个实例，说明能抵抗反射攻击（极少见）
                log.info("✅ {} 能够抵抗反射攻击！", singletonName);
            }
        } catch (Exception e) {
            // 捕获所有反射相关的异常：
            // - NoSuchMethodException: 找不到构造函数
            // - IllegalAccessException: 无法访问构造函数
            // - InstantiationException: 无法实例化
            // - InvocationTargetException: 构造函数抛出异常
            log.error("反射创建实例失败：{}", e.getMessage());
            
            // 包装为运行时异常并抛出
            throw new RuntimeException(e);
        }
    }
}
