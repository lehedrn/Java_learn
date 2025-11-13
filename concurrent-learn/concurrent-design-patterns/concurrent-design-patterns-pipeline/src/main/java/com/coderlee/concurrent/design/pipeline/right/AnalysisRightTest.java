package com.coderlee.concurrent.design.pipeline.right;

/**
 * 并发流水线设计模式 - 正确用法演示类
 * <p>
 * 启动一个 {@link AnalysisTask} 线程以展示如何使用自定义流水线框架进行并发任务调度与处理。
 * </p>
 *
 * @see AnalysisTask
 */
public class AnalysisRightTest {

    /**
     * 程序入口点
     * <p>
     * 创建并启动一个分析任务线程，随后阻塞主线程直到子线程执行完毕。
     * </p>
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        AnalysisTask analysisTask = new AnalysisTask(5);
        Thread thread = new Thread(analysisTask);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

