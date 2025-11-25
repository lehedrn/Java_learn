package com.coderlee.juc1.utils;

import java.util.concurrent.TimeUnit;

public class SleepUtils {
    /**
     * 工具方法，使当前线程睡眠指定毫秒数
     *
     * @param timeout 睡眠时间(毫秒)
     * @throws RuntimeException 如果线程在睡眠期间被中断，则包装InterruptedException抛出
     */
    public static void sleep(long timeout) {
        try {
            // 使用TimeUnit.MILLISECONDS进行精确的时间控制，使线程睡眠指定毫秒数
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            // 当线程在睡眠期间被中断时，捕获InterruptedException并重新抛出为RuntimeException
            // 避免强制调用者处理受检异常，同时保证异常不会被忽略
            throw new RuntimeException(e);
        }
    }
}
