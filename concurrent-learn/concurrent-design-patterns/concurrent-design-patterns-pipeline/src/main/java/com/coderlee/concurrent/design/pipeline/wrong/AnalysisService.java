package com.coderlee.concurrent.design.pipeline.wrong;

import java.util.List;

/**
 * 数据分析服务接口
 *
 * 该接口定义了数据分析的基本操作，用于处理字符串列表并返回统计结果
 *
 * @author coderlee
 * @see AnalysisServiceImpl 实现类
 */
public interface AnalysisService {

    /**
     * 分析输入数据列表并计算总和
     *
     * @param input 输入的数据列表，每个元素格式为"ID-数值"
     * @return 所有数值部分的总和
     */
    long analysis(List<String> input);
}
