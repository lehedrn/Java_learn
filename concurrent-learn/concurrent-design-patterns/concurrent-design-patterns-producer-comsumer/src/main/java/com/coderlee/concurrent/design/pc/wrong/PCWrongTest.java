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

/**
 * 错误的生产者消费者模式示例1 - 串行执行
 *
 * <p>此示例展示了不正确的实现方式，所有的操作都是串行执行的，
 * 没有利用并发的优势。适用于演示为什么需要生产者消费者模式。</p>
 *
 * @see DBService
 * @see UploadService
 * @see IndexService
 */
@Slf4j
public class PCWrongTest {

    /**
     * 主函数，程序入口点
     *
     * <p>按顺序执行数据库保存、文件上传和建立索引操作，
     * 每个步骤必须等待前一个步骤完成后才能开始。</p>
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 记录总体任务开始时间
        log.info("总体任务开始");
        Instant start = Instant.now();

        // 创建数据库服务实例并保存数据
        DBService dbService = new DBServiceImpl();
        dbService.save("coderleeSaveData");

        // 创建上传服务实例并上传文件
        UploadService uploadService = new UploadServiceImpl();
        uploadService.upload("coderleeUploadFile001", "coderleeUploadFile002");

        // 创建索引服务实例并建立索引
        IndexService indexService = new IndexServiceImpl();
        indexService.index("coderleeUploadFile001", "coderleeUploadFile002");

        // 输出总体任务耗时
        log.info("总体任务结束, 耗时: {} ms", Duration.between(start, Instant.now()).toMillis());
    }
}
