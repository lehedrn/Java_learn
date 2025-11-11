package com.coderlee.concurrent.design.promise.common.service;

import com.coderlee.concurrent.design.promise.common.domain.Coupon;

/**
 * 优惠券服务接口
 *
 * 定义了优惠券相关的服务操作，主要用于发送优惠券。
 *
 * @author coderlee
 * @see Coupon 优惠券实体类
 */
public interface CouponService {
    /**
     * 发送优惠券
     *
     * 该方法用于发送优惠券给用户，返回一个表示发送状态的优惠券对象。
     *
     * @return {@link Coupon} 优惠券对象，包含发送状态信息
     * @see Coupon 优惠券实体类
     */
    Coupon sendCoupon();
}
