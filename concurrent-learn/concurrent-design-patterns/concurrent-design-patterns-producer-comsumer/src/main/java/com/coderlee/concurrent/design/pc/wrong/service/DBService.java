package com.coderlee.concurrent.design.pc.wrong.service;

/**
 * 数据库服务接口
 *
 * <p>定义了保存业务数据的操作规范。</p>
 */
public interface DBService {

    /**
     * 保存业务数据
     *
     * @param businessData 要保存的业务数据
     */
    void save(String businessData);
}
