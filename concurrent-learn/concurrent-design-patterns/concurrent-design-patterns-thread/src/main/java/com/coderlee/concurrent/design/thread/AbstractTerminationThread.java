package com.coderlee.concurrent.design.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * 支持终止操作的抽象线程类
 * <p>
 * 扩展了标准Thread类并实现了{@link Termination}接口，提供了线程终止的基础实现。
 * 子类可以通过检查{@link TerminationToken}的状态来实现优雅的线程终止。
 * </p>
 *
 * @see Thread Java标准线程类
 * @see Termination 线程终止接口
 * @see TerminationToken 线程终止令牌类
 */
@Slf4j
public abstract class AbstractTerminationThread extends Thread implements Termination {
    /**
     * 线程共享停止的标志实例对象
     */
    public final TerminationToken terminationToken;

    public AbstractTerminationThread() {
        this(new TerminationToken());
    }

    public AbstractTerminationThread(TerminationToken terminationToken) {
        this.terminationToken = terminationToken;
        log.info("注册线程到coordinatedThreads队列中");
        terminationToken.register(this);
    }

    @Override
    public void terminate() {
        //设置标志实例对象为true
        log.info("设置中断标志对象为中断状态");
        this.terminationToken.setToShutdown(true);

        try {
            doTerminate();
        } finally {
            // 如果没有等待的任务，则强制去停止线程
            if (terminationToken.noExecuteTaskCount.get() <= 0) {
                super.interrupt();
            }
        }
    }


    @Override
    public void run() {
        Exception ex = null;
        try {
            for (;;) {
                //先判断中断实例的标识是否为true，同时没有未完成的任务
                log.info("执行线程的逻辑，此时中断标志位：{}, 未完成的任务数量：{}", terminationToken.isToShutdown(), terminationToken.noExecuteTaskCount.get());
                if (terminationToken.isToShutdown() && terminationToken.noExecuteTaskCount.get() <= 0) {
                    //所有线程已经终止，跳出循环
                    log.info("中断标志为true，没有未完成的任务，线程退出");
                    break;
                }
                // 执行具体的业务逻辑
                doRun();
            }
        } catch (Exception e) {
            // 中断线程可能给调用interrupt被中断
            ex = e;
            if (e instanceof InterruptedException) {
                // 中断线程响应退出
                log.error("中断响应", e);
            }
        } finally {
            try {
                log.info("线程停止，回调终止后的清理工作");
                doCleanup(ex);
            } finally {
                // 通知terminationToken管理的所有线程实例退出
                log.info("标志实例对象中一个线程终止，通知其他线程终止");
                terminationToken.notifyThreadTermination(this);
            }
        }
    }

    /**
     * 具体逻辑留给子类实现，执行终止线程的逻辑
     */
    protected void doTerminate() {
    }

    /**
     * 具体逻辑留给子类实现，完成线程终止后的一些清理动作
     */
    protected void doCleanup(Exception ex) {
    }

    /**
     * 留给子类去实现具体的线程业务逻辑
     */
    protected abstract void doRun() throws InterruptedException;

}
