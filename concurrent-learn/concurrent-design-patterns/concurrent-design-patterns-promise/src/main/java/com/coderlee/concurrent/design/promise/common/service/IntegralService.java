package com.coderlee.concurrent.design.promise.common.service;

import com.coderlee.concurrent.design.promise.common.domain.Integral;

/**
 * 积分服务接口
 *
 * 定义了积分相关的服务操作，主要用于发送积分。
 *
 * @author coderlee
 * @see Integral 积分实体类
 */
public interface IntegralService {
    /**
     * 发送积分
     *
     * 该方法用于发送积分给用户，返回一个表示发送状态的积分对象。
     *
     * @return {@link Integral} 积分对象，包含发送状态信息
     * @see Integral 积分实体类
     */
    Integral sendIntegral();
}
