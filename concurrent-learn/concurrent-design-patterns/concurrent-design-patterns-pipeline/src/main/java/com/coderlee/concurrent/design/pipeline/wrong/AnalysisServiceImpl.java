package com.coderlee.concurrent.design.pipeline.wrong;

import java.util.List;

/**
 * 数据分析服务实现类
 *
 * 该类实现了{@link AnalysisService}接口，提供了具体的数据分析逻辑。
 * 注意：这是一个错误示例，展示了不正确的并发处理方式
 *
 * @author coderlee
 * @see AnalysisService 接口定义
 */
public class AnalysisServiceImpl implements AnalysisService {

    /**
     * 分析输入数据列表并计算总和
     *
     * 该方法遍历输入列表中的每个字符串，解析出数值部分并累加
     * 字符串格式应为"ID-数值"，其中ID和数值用"-"分隔
     *
     * @param input 输入的数据列表，每个元素格式为"ID-数值"
     * @return 所有数值部分的总和
     */
    @Override
    public long analysis(List<String> input) {
        // 初始化总和为0
        long sum = 0;

        // 检查输入是否为空或空列表
        if (null == input || input.isEmpty()) {
            // 如果输入为空，直接返回0
            return sum;
        }

        // 遍历输入列表中的每个元素
        for (String i : input) {
            // 使用"-"分割字符串，获取数值部分
            String[] arr = i.split("-");

            // 将数值部分转换为long类型并累加到总和中
            sum += Long.parseLong(arr[1]);
        }

        // 返回计算得到的总和
        return sum;
    }

}
