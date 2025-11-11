package com.coderlee.concurrent.design.pc.wrong.service;

/**
 * 索引服务接口
 *
 * <p>定义了为文件建立索引的操作规范。</p>
 */
public interface IndexService {

    /**
     * 为一个或多个文件建立索引
     *
     * @param fileNames 要建立索引的文件名数组
     */
    void index(String... fileNames);
}
