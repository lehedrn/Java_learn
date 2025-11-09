package com.coderlee.concurrent.design.wrong;

/**
 * 错误计数器类，用于演示非线程安全的计数器实现。
 * <p>
 * 该类通过一个整型变量维护访问计数，但由于缺乏同步机制，
 * 在多线程环境下会导致计数不准确的问题。
 * </p>
 * 
 * @see #accessVisit()
 * @see #getVisitCount()
 */
public class WrongCounter {

    /**
     * 访问计数变量，初始值为0。
     * <p>
     * 由于没有使用任何同步手段，该变量在多线程环境下的递增操作是非线程安全的。
     * </p>
     */
    private int visiteCount;

    /**
     * 增加访问计数。
     * <p>
     * 该方法直接对 {@link #visiteCount} 进行递增操作，未考虑并发问题，
     * 因此在多线程场景下可能导致数据竞争和错误的结果。
     * </p>
     */
    public void accessVisit() {
        // 对访问计数进行递增操作（注意：非原子性操作，包含了三个步骤：读取、递增、写入）
        visiteCount++;
    }

    /**
     * 获取当前的访问计数值。
     * <p>
     * 返回 {@link #visiteCount} 的当前值，可能在多线程环境下返回不一致的结果。
     * </p>
     *
     * @return 当前的访问计数值
     */
    public int getVisitCount() {
        // 返回访问计数的当前值
        return visiteCount;
    }
}