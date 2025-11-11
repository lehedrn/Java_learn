package com.coderlee.concurrent.design.promise.common.service.impl;

import java.util.concurrent.TimeUnit;

import com.coderlee.concurrent.design.promise.common.domain.Coupon;
import com.coderlee.concurrent.design.promise.common.service.CouponService;

/**
 * 优惠券服务实现类
 * 实现了优惠券服务接口，提供了发送优惠券的具体实现。
 * @author coderlee
 * @see CouponService 优惠券服务接口
 */
public class CouponServiceImpl implements CouponService {

    /**
     * 发送优惠券的实现方法
     * 模拟发送优惠券的过程，会休眠5秒钟来模拟网络延迟或处理时间，
     * 然后将优惠券状态设置为true表示发送成功。
     * @return {@link Coupon} 发送成功的优惠券对象
     * @see CouponService#sendCoupon()
     * @see Coupon 优惠券实体类
     */
    @Override
    public Coupon sendCoupon() {
        // 创建一个新的优惠券对象
        Coupon coupon = new Coupon();
        try {
            // 模拟发送过程需要5秒钟
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            // 如果线程被中断，则抛出运行时异常
            throw new RuntimeException(e);
        }
        // 设置优惠券状态为已发送
        coupon.setStatus(true);
        // 返回发送成功的优惠券对象
        return coupon;
    }

}
