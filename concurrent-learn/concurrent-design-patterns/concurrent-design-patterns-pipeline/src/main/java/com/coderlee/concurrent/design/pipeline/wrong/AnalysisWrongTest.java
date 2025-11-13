package com.coderlee.concurrent.design.pipeline.wrong;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 错误示例测试类
 *
 * 该类用于演示不正确的并发处理方式，作为反面教材展示
 * 测试了{@link AnalysisServiceImpl}的基本功能
 *
 * @author coderlee
 * @see AnalysisServiceImpl 被测试的实现类
 */
@Slf4j
public class AnalysisWrongTest {

    /**
     * 主函数，程序入口点
     *
     * 创建{@link AnalysisServiceImpl}实例并执行数据分析操作，
     * 最后记录并输出结果日志
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建数据分析服务实例
        AnalysisService analysisService = new AnalysisServiceImpl();

        // 准备测试数据并执行分析操作
        List<String> testData = Arrays.asList("1001-234", "1002-456", "1003-198", "1004-132");
        Long result = analysisService.analysis(testData);

        // 记录并输出分析结果
        log.info("统计出的结果数据为: {}", result);
    }
}
