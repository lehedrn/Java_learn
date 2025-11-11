package com.coderlee.concurrent.design.promise.common.service.impl;

import java.util.concurrent.TimeUnit;

import com.coderlee.concurrent.design.promise.common.domain.Integral;
import com.coderlee.concurrent.design.promise.common.service.IntegralService;

/**
 * 积分服务实现类
 * 实现了积分服务接口，提供了发送积分的具体实现。
 * @author coderlee
 * @see IntegralService 积分服务接口
 */
public class IntegralServiceImpl implements IntegralService {

    /**
     * 发送积分的实现方法
     * 模拟发送积分的过程，会休眠10秒钟来模拟网络延迟或处理时间，
     * 然后将积分状态设置为true表示发送成功。
     * @return {@link Integral} 发送成功的积分对象
     * @see IntegralService#sendIntegral()
     * @see Integral 积分实体类
     */
    @Override
    public Integral sendIntegral() {
        // 创建一个新的积分对象
        Integral integral = new Integral();
        try {
            // 模拟发送过程需要10秒钟
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            // 如果线程被中断，则抛出运行时异常
            throw new RuntimeException(e);
        }
        // 设置积分状态为已发送
        integral.setStatus(true);
        // 返回发送成功的积分对象
        return integral;
    }

}
