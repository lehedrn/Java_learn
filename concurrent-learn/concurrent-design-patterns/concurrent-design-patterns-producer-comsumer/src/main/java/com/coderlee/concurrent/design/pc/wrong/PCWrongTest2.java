package com.coderlee.concurrent.design.pc.wrong;

import com.coderlee.concurrent.design.pc.wrong.impl.DBServiceImpl;
import com.coderlee.concurrent.design.pc.wrong.impl.IndexServiceImpl;
import com.coderlee.concurrent.design.pc.wrong.impl.UploadServiceImpl;
import com.coderlee.concurrent.design.pc.wrong.service.DBService;
import com.coderlee.concurrent.design.pc.wrong.service.IndexService;
import com.coderlee.concurrent.design.pc.wrong.service.UploadService;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 错误的生产者消费者模式示例2 - 不当的并发使用
 *
 * <p>
 *     此示例展示了另一种不正确的实现方式，虽然部分操作使用了并发，
 * </p>
 *
 * @see Executors
 * @see Future
 */
@Slf4j
public class PCWrongTest2 {

    /**
     * 主函数，程序入口点
     *
     * <p>先串行执行数据库保存和文件上传操作，
     * 再将建立索引操作提交到线程池中异步执行。</p>
     *
     * @param args 命令行参数
     * @throws InterruptedException 如果线程在等待过程中被中断
     */
    public static void main(String[] args) throws InterruptedException {
        // 记录总体任务开始
        log.info("总体任务开始");

        // 创建缓存线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();
        Instant start = Instant.now();

        // 串行执行数据库保存操作
        DBService dbService = new DBServiceImpl();
        dbService.save("coderleeSaveData");

        // 串行执行文件上传操作
        UploadService uploadService = new UploadServiceImpl();
        uploadService.upload("coderleeUploadFile001", "coderleeUploadFile002");

        // 将建立索引操作提交到线程池中异步执行
        Future<?> future = threadPool.submit(() -> {
            IndexService indexService = new IndexServiceImpl();
            indexService.index("coderleeUploadFile001", "coderleeUploadFile002");
        });

        // 输出返回用户结果的时间（不包括索引建立）
        log.info("返回用户结果耗时: {} ms", Duration.between(start, Instant.now()).toMillis());

        try {
            // 等待索引建立完成
            future.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        // 关闭线程池
        threadPool.shutdown();

        // 输出总体任务耗时
        log.info("总体任务结束, 耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
    }
}
