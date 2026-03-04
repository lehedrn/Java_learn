package com.coderlee.designpattern.creational.singleton;

import com.coderlee.designpattern.creational.singleton.supports.SingletonTestUtil;

/**
 * 单例模式示例 08 - 枚举实现（最简洁的单例模式）
 * <p>
 * 特点：
 * 1. 线程安全：由 JVM 保证枚举类型的线程安全性
 * 2. 防止反射攻击：枚举类型无法通过反射创建新实例
 * 3. 防止序列化破坏：枚举类型自动处理序列化问题
 * 4. 实现简洁：代码量最少，语义清晰
 * </p>
 * <p>
 * 优点：
 * 1. 最安全的单例模式实现方式
 * 2. 天然支持序列化和反序列化
 * 3. 防止反射攻击
 * 4. 代码简洁，易于维护
 * </p>
 * <p>
 * 适用场景：
 * 推荐使用这种方式实现单例模式，特别是需要考虑安全性和简洁性的场景
 * </p>
 *
 * @author coderlee
 * @version 1.0
 */
public enum SingletonExample08 {
    /**
     * 枚举常量，即单例实例
     * 在枚举类加载时创建，由 JVM 保证线程安全
     * 这是唯一的实例，无法创建更多实例
     */
    INSTANCE;

    /**
     * 私有构造函数（可选）
     * 枚举类型的构造函数默认就是私有的
     * 即使不显式声明，外部也无法调用
     */
    SingletonExample08() {}

    /**
     * 主方法，用于测试枚举单例模式的特性
     * 包括：验证线程安全、验证单例性、测试反射攻击（枚举免疫反射攻击）
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 测试线程安全性：多线程环境下验证是否只有一个实例
        // 枚举类型天然线程安全，测试一定会通过
        SingletonTestUtil.testSingletonThreadSafety(SingletonExample08.class.getSimpleName(), () -> SingletonExample08.INSTANCE);

        // 测试单例性：验证两次获取的是同一个实例
        // 枚举类型保证单例性，测试一定会通过
        SingletonTestUtil.testSingleton(SingletonExample08.class.getSimpleName(), () -> SingletonExample08.INSTANCE);

        // 测试反射攻击：验证反射是否能破坏单例模式
        // 枚举类型可以免疫反射攻击，这是枚举实现的独特优势
        // 尝试通过反射创建枚举实例会抛出异常
        SingletonTestUtil.testReflection(SingletonExample08.class.getSimpleName(), SingletonExample08.class, () -> SingletonExample08.INSTANCE);
        // 注释说明：反射无法创建枚举类型的实例
        // 原因：Java 的枚举类型在底层做了特殊处理，防止反射攻击
    }
}

