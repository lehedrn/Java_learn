package com.coderlee.concurrent.chapter10;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>演示如何使用 `Unsafe` 类直接操作内存中的字段值。</p>
 * <p>该类通过 `Unsafe` 实现了对静态字段和实例字段的修改，并展示了修改前后的值对比。</p>
 * <p><strong>注意：</strong>`Unsafe` 是一个不安全的工具类，通常不推荐在普通业务代码中使用，主要用于底层开发或框架实现。</p>
 */
@Slf4j
public class UnsafeTest {

    // 获取 `Unsafe` 实例，用于直接操作内存
    private static final Unsafe unsafe = getUnsafe();

    // 静态字段 [staticName] 的内存偏移量
    private static long staticNameOffset = 0;
    // 实例字段 [memberVariable] 的内存偏移量
    private static long memberVariableOffset = 0;

    // 被操作的静态字段
    private static String staticName = "leehd_001";
    // 被操作的实例字段
    private String memberVariable = "leehd_001";

    static {
        try {
            // 计算静态字段 [staticName] 的内存偏移量
            staticNameOffset = unsafe.staticFieldOffset(UnsafeTest.class.getDeclaredField("staticName"));
            // 计算实例字段 [memberVariable] 的内存偏移量
            memberVariableOffset = unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("memberVariable"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主方法，用于展示如何通过 `Unsafe` 修改静态字段和实例字段的值。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        UnsafeTest unSaveTest = new UnsafeTest();

        // 打印修改前的字段值
        log.info("修改前的值: staticName={}, memberVariable={}", staticName, unSaveTest.memberVariable);

        // 使用 `Unsafe.putObject` 修改静态字段 [staticName] 的值
        unsafe.putObject(UnsafeTest.class, staticNameOffset, "leehd_static");
        // 使用 `Unsafe.compareAndSwapObject` 修改实例字段 [memberVariable] 的值
        unsafe.compareAndSwapObject(unSaveTest, memberVariableOffset, "leehd_001", "leehd_variable");

        // 打印修改后的字段值
        log.info("修改后的值: staticName={}, memberVariable={}", staticName, unSaveTest.memberVariable);
    }

    /**
     * 获取 `Unsafe` 实例。
     * <p>由于 `Unsafe` 的构造方法是私有的，且其单例实例存储在私有字段 `theUnsafe` 中，
     * 因此需要通过反射获取其实例。</p>
     *
     * @return `Unsafe` 实例
     */
    private static Unsafe getUnsafe() {
        Unsafe unsafe = null;
        try {
            // 获取 `Unsafe` 类中存储单例的私有字段 `theUnsafe`
            Field singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
            // 设置字段可访问
            singleoneInstanceField.setAccessible(true);
            // 获取字段值（即 `Unsafe` 实例）
            unsafe = (Unsafe) singleoneInstanceField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unsafe;
    }
}