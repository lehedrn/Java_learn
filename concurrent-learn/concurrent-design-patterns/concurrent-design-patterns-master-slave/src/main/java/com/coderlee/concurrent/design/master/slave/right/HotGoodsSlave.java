package com.coderlee.concurrent.design.master.slave.right;

import com.coderlee.concurrent.design.thread.AbstractTerminationThread;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Slave节点，用于接收并执行由Master下发的日志解析任务，
 * 并将解析得到的商品访问次数更新到共享的统计数据中。
 *
 * @see AbstractTerminationThread
 * @author coderlee
 */
@Slf4j
public class HotGoodsSlave extends AbstractTerminationThread {

    /**
     * 存放待处理任务的阻塞队列
     */
    private final BlockingQueue<BufferedReader> workQueue;

    /**
     * 多线程安全的商品访问次数统计容器
     */
    private final ConcurrentMap<String, AtomicInteger> hotGoodsMap;

    /**
     * 创建一个新的工作节点实例。
     *
     * @param hotGoodsMap 用于保存最终统计结果的并发映射
     */
    public HotGoodsSlave(ConcurrentMap<String, AtomicInteger> hotGoodsMap) {
        this.hotGoodsMap = hotGoodsMap;
        this.workQueue = new ArrayBlockingQueue<>(128);
    }

    /**
     * 执行实际的日志解析逻辑。
     * 取出队列中的日志文件流，逐行读取并解析商品信息，
     * 更新商品访问计数。
     *
     * @throws InterruptedException 当前线程被中断时抛出
     */
    @Override
    protected void doRun() throws InterruptedException {
        BufferedReader bufferedReader = workQueue.take();
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineArray = line.split("-");
                HotGoodsLog hotGoodsLog = new HotGoodsLog(lineArray[0], lineArray[1]);
                log.info("读取到的文件内容为: {}", hotGoodsLog.toString());
                AtomicInteger hotGoodsCount = hotGoodsMap.putIfAbsent(hotGoodsLog.getGoodsId(), new AtomicInteger(1));
                if (Objects.isNull(hotGoodsCount)) {
                    hotGoodsCount = new AtomicInteger();
                }
                hotGoodsCount.incrementAndGet();
            }
        } catch (Exception e) {
            log.error("处理文件异常", e);
        } finally {
            terminationToken.noExecuteTaskCount.decrementAndGet();
            try {
                bufferedReader.close();
            } catch (IOException e) {
                log.error("关闭文件流异常", e);
            }
        }
    }

    /**
     * 提交新的日志解析任务到内部工作队列。
     *
     * @param task 待处理的缓冲读取器（代表一个日志文件）
     */
    public void submitTask(BufferedReader task) {
        try {
            this.workQueue.put(task);
            terminationToken.noExecuteTaskCount.incrementAndGet();
        } catch (InterruptedException e) {
            log.error("添加任务异常", e);
        }
    }
}
