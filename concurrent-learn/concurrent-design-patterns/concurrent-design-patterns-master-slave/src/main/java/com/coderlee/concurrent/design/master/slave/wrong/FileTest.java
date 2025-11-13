package com.coderlee.concurrent.design.master.slave.wrong;

import java.io.File;

/**
 * 文件服务测试类
 * <p>
 * 用于测试 {@link FileService} 接口及其实现类 {@link FileServiceImpl} 的功能
 * </p>
 *
 * @author coderlee
 * @since 1.0
 * @see FileService
 * @see FileServiceImpl
 */
public class FileTest {

    /**
     * 主方法，程序入口
     * <p>
     * 创建 {@link FileServiceImpl} 实例并测试读取分析多个文件的功能
     * </p>
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 创建文件服务实例
        FileService fileService = new FileServiceImpl();

        // 调用服务方法读取并分析多个文件
        fileService.readAnalysisGoods("coderlee-001", "coderlee-002", "coderlee-003", "coderlee-004", "coderlee-005");
    }
}
