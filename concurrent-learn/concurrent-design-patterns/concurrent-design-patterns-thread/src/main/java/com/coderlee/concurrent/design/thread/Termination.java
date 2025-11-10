package com.coderlee.concurrent.design.thread;

/**
 * 线程终止接口
 * <p>
 * 定义线程终止的标准方法，实现该接口的类需要提供具体的线程终止逻辑。
 * 通常与{@link TerminationToken}配合使用，实现线程间的协调终止。
 * </p>
 *
 * @see TerminationToken 线程终止令牌类
 */
public interface Termination {
    /**
     * 终止线程操作
     * <p>
     * 实现类应在此方法中定义如何安全地终止线程执行
     * </p>
     */
    void terminate();
}
