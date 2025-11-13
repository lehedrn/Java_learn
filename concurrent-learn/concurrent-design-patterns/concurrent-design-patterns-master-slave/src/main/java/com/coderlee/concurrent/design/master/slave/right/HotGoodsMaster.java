package com.coderlee.concurrent.design.master.slave.right;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Master-Slave模式下的主控节点，负责管理多个工作节点（{@link HotGoodsSlave}），
 * 并协调它们对日志数据的分析与汇总。
 *
 * @author coderlee
 */
public class HotGoodsMaster {

    /**
     * 日志文件所在根路径
     */
    @Getter
    private final String logFileRootPath;

    /**
     * 每个任务包含的日志文件数量
     */
    private static final int FILE_COUNT = 1;

    /**
     * 启动的工作线程数（即 Slave 数量）
     */
    private static final int SLAVE_COUNT = 2;

    /**
     * 创建一个新的 Master 节点实例。
     *
     * @param logFileRootPath 日志文件存储的根路径
     */
    public HotGoodsMaster(String logFileRootPath) {
        this.logFileRootPath = logFileRootPath;
    }

    /**
     * 分析日志文件并获取各商品的访问次数统计结果。
     *
     * @param bufferedReader 提供待分析的日志文件列表
     * @return 统计后的商品访问次数映射表
     * @throws IOException IO操作失败时抛出
     */
    public ConcurrentMap<String, AtomicInteger> getHotGoods(BufferedReader bufferedReader) throws IOException {
        ConcurrentMap<String, AtomicInteger> hotGoodsMap = new ConcurrentHashMap<>();
        HotGoodsSlave[] hotGoodsSlaves = this.createSlave(hotGoodsMap);
        this.dispatchTask(bufferedReader, hotGoodsSlaves);
        for (int i = 0; i < SLAVE_COUNT; i++) {
            hotGoodsSlaves[i].terminate();
        }
        return hotGoodsMap;
    }

    /**
     * 将日志文件分发给不同的工作节点进行处理。
     *
     * @param bufferedReader 日志文件列表读取器
     * @param hotGoodsSlaves 所有可用的工作节点数组
     * @throws IOException IO操作失败时抛出
     */
    private void dispatchTask(BufferedReader bufferedReader, HotGoodsSlave[] hotGoodsSlaves) throws IOException {
        String line;
        Set<String> fileNames = new HashSet<>();
        int fileCount = 0;
        int slaveIndex = -1;
        BufferedReader fileReader;
        while ((line = bufferedReader.readLine()) != null){
            fileNames.add(line);
            fileCount++;
            if (fileCount % FILE_COUNT == 0){
                slaveIndex = (slaveIndex + 1) % SLAVE_COUNT;
                fileReader = getFileReadByFileNames(fileNames);
                // 提交任务至对应工作节点
                hotGoodsSlaves[slaveIndex].submitTask(fileReader);
                fileNames.clear();
                fileCount = 0;
            }
        }
        if (fileCount > 0){
            fileReader = getFileReadByFileNames(fileNames);
            slaveIndex = (slaveIndex + 1) % SLAVE_COUNT;
            hotGoodsSlaves[slaveIndex].submitTask(fileReader);
        }

    }

    /**
     * 初始化并启动所有工作节点。
     *
     * @param hotGoodsMap 共享的结果存储结构
     * @return 初始化后的工作节点数组
     */
    private HotGoodsSlave[] createSlave(ConcurrentMap<String, AtomicInteger> hotGoodsMap) {
        HotGoodsSlave[] hotGoodsSlaves = new HotGoodsSlave[SLAVE_COUNT];
        HotGoodsSlave slave;
        for (int i = 0; i < SLAVE_COUNT; i++) {
            slave = new HotGoodsSlave(hotGoodsMap);
            hotGoodsSlaves[i] = slave;
            slave.start();
        }
        return hotGoodsSlaves;
    }

    /**
     * 使用文件名集合构建顺序读取的缓冲输入流。
     *
     * @param fileNames 待读取的文件名称集合
     * @return 对应于这些文件内容的合并输入流包装成的缓冲读取器
     */
    private BufferedReader getFileReadByFileNames(Set<String> fileNames) {
        return new BufferedReader(new InputStreamReader(new SequenceInputStream(new HotGoodsEnumeration(fileNames, logFileRootPath))));
    }

}

