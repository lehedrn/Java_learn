package com.coderlee.concurrent.design.master.slave.right;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试类，演示如何使用 {@link HotGoodsMaster} 和 {@link HotGoodsSlave}
 * 来分析日志文件并输出商品热度排名。
 *
 * @author coderlee
 */
@Slf4j
public class HotGoodsTest {

    /**
     * 日志文件所在根路径常量
     */
    private static final String LOG_FILE_ROOT_PATH = "/home/workspace/coderlee/java_projects/Java_learn/concurrent-learn/concurrent-design-patterns/concurrent-design-patterns-master-slave/src/main/resources/logs/";

    /**
     * 应用程序入口函数。
     *
     * @param args 命令行参数
     * @throws IOException IO操作失败时抛出
     * @throws InterruptedException 线程等待过程中被打断时抛出
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        HotGoodsMaster hotGoodsMaster = new HotGoodsMaster(LOG_FILE_ROOT_PATH);

        // 加载待分析的日志文件清单
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        Files.newInputStream(Paths.get(hotGoodsMaster.getLogFileRootPath() + "analysis.log"))
                )
        );

        // 开始分析并获取结果
        ConcurrentMap<String, AtomicInteger> resultMap = hotGoodsMaster.getHotGoods(bufferedReader);

        // 等待所有任务完成（简单模拟）
        Thread.sleep(2000);

        // 输出商品热度排行
        for (Map.Entry<String, AtomicInteger> entry : resultMap.entrySet()){
            log.info("{}===>>>{}", entry.getKey(), entry.getValue().get());
        }
    }
}

