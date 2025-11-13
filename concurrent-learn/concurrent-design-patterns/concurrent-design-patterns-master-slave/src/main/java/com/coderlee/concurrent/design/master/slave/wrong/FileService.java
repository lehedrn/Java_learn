package com.coderlee.concurrent.design.master.slave.wrong;

import java.util.Map;

/**
 * 文件服务接口
 * <p>
 * 定义了读取并分析商品数据文件的操作规范
 * </p>
 *
 * @author coderlee
 * @since 1.0
 */
public interface FileService {

    /**
     * 读取并分析指定的商品数据文件
     * <p>
     * 此方法会读取多个文件并统计其中的商品信息
     * </p>
     *
     * @param fileNames 需要读取分析的文件名数组
     * @return 包含商品名称和对应数量的映射关系
     */
    Map<String, Integer> readAnalysisGoods(String... fileNames);
}
