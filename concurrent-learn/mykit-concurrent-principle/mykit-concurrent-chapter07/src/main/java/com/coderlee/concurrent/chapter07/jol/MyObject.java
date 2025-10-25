package com.coderlee.concurrent.chapter07.jol;

/**
 * MyObject 是一个简单的 Java 类，用于演示对象的内存布局。
 * 它包含一个私有字段 count 和相关的 getter/setter 方法。
 */
public class MyObject {

    private int count = 0; // 私有字段，表示计数值，默认值为 0

    /**
     * 获取当前对象的 count 值。
     *
     * @return 当前 count 的值
     */
    public int getCount() {
        return count;
    }

    /**
     * 设置当前对象的 count 值。
     *
     * @param count 要设置的新值
     */
    public void setCount(int count) {
        this.count = count; // 更新 count 字段的值
    }
}