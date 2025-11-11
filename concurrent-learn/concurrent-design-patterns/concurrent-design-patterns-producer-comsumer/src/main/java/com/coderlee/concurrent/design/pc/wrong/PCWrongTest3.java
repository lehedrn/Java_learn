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
import java.util.concurrent.Future;

/**
 * 错误的生产者消费者模式示例3 - 使用自定义线程池但仍有问题
 *
 * <p>
 *     此示例使用了自定义的线程池 {@link PCThreadPool} 来执行部分操作，
 * </p>
 *
 * @see PCThreadPool
 */
@Slf4j
public class PCWrongTest3 {

    /**
     * 主函数，程序入口点
     *
     * <p>先串行执行数据库保存和文件上传操作，
     * 再使用自定义线程池异步执行建立索引操作。</p>
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 记录总体任务开始
        log.info("总体任务开始");
        Instant start = Instant.now();

        // 串行执行数据库保存操作
        DBService dbService = new DBServiceImpl();
        dbService.save("coderleeSaveData");

        // 串行执行文件上传操作
        UploadService uploadService = new UploadServiceImpl();
        uploadService.upload("coderleeUploadFile001", "coderleeUploadFile002");

        // 使用自定义线程池异步执行建立索引操作
        Future<?> future = PCThreadPool.submit(() -> {
            IndexService indexService = new IndexServiceImpl();
            indexService.index("coderleeUploadFile001", "coderleeUploadFile002");
        });

        // 输出返回用户结果的时间（不包括索引建立）
        log.info("返回用户结果耗时: {} ms", Duration.between(start, Instant.now()).toMillis());

        try {
            // 等待索引建立完成
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 关闭线程池
        PCThreadPool.shutdown();

        // 输出总体任务耗时
        log.info("总体任务结束, 耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
    }
}
