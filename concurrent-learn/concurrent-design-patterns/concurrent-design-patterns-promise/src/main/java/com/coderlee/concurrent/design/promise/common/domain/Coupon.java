package com.coderlee.concurrent.design.promise.common.domain;

import lombok.Data;

/**
 * 优惠券实体类
 * 该类表示一个优惠券对象，包含优惠券的状态信息。
 * @author coderlee
 */
@Data
public class Coupon {
    /**
     * 优惠券状态，默认为false（未激活）
     * true表示已发送或已激活，false表示未发送或未激活
     */
    private boolean status = false;
}
