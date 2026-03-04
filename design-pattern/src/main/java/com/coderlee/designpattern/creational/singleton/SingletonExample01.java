package com.coderlee.designpattern.creational.singleton;

import com.coderlee.designpattern.creational.singleton.supports.SingletonTestUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 单例模式示例 01 - 饿汉式实现（静态常量式）
 * <p>
 * 特点：
 * 1. 在类加载时就创建实例，无需担心线程安全问题
 * 2. 实例一旦创建就不会释放，即使未被使用
 * 3. 适用于实例创建成本低或需要预加载的场景
 * </p>
 * 
 * @author coderlee
 * @version 1.0
 */
@Slf4j
public class SingletonExample01 /*implements Serializable*/ {
    /**
     * 私有构造函数，防止外部通过 new 关键字创建实例
     * 这是单例模式的核心要素之一
     */
    private SingletonExample01() {}

    /**
     * 静态常量实例，在类加载时立即初始化
     * final 关键字确保引用不可被修改
     * JVM 类加载机制保证线程安全
     */
    private static final SingletonExample01 INSTANCE = new SingletonExample01();

    /**
     * 获取单例实例的全局访问点
     * 提供对外访问的唯一入口，返回预先创建的实例
     *
     * @return 唯一的 SingletonExample01 实例
     */
    public static SingletonExample01 getInstance() {
        return INSTANCE;
    }

    /**
     * 主方法，用于测试单例模式的正确性
     * 包括：验证单例性、测试反射攻击
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // ========== 测试一：验证单例模式的正确性 ==========
        
       /* // 第一次获取单例实例
        SingletonExample01 instance1 = SingletonExample01.getInstance();
        // 第二次获取单例实例
        SingletonExample01 instance2 = SingletonExample01.getInstance();
        
        // 验证两次获取的是同一个实例（内存地址相同）
        log.info("instance1 {} instance2", instance1 == instance2 ? "==" : "!=");
        // 输出第一个实例的哈希码
        log.info("instance1 hashcode: {} ", instance1.hashCode());
        // 输出第二个实例的哈希码
        log.info("instance2 hashcode: {} ", instance2.hashCode());*/
        SingletonTestUtil.testSingleton(SingletonExample01.class.getSimpleName(), SingletonExample01::getInstance);

        // ========== 测试二：使用反射破坏单例模式 ==========
        
        /*log.info("========== 使用反射破坏 ==========");
        try {
            // 获取私有构造函数的对象
            Constructor<SingletonExample01> declaredConstructors = SingletonExample01.class.getDeclaredConstructor();
            // 暴力反射：设置私有构造函数可访问（绕过 Java 访问控制检查）
            declaredConstructors.setAccessible(true);
            // 通过反射创建新的实例（这会破坏单例）
            SingletonExample01 instance3 = declaredConstructors.newInstance();
            // 验证反射创建的实例与正常获取的实例是否为同一个
            log.info("instance3 {} instance2", instance3 == instance2 ? "==" : "!=");
            // 输出反射创建实例的哈希码
            log.info("instance3 hashcode: {} ", instance3.hashCode());
            // 结论：反射会破坏饿汉式单例模式
            log.info("使用反射会破坏饿汉式单例模式");
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            // 处理反射相关的异常
            throw new RuntimeException(e);
        }*/
        SingletonTestUtil.testReflection(SingletonExample01.class.getSimpleName(), SingletonExample01.class, SingletonExample01::getInstance);
        // 验证序列化破坏单例模式，需要将单例实现 Serializable 接口
//        SingletonTestUtil.testSerialization(SingletonExample01.class.getSimpleName(), SingletonExample01::getInstance);
//        SingletonTestUtil.testClone(SingletonExample01.class.getSimpleName(), SingletonExample01::getInstance);
    }
}
