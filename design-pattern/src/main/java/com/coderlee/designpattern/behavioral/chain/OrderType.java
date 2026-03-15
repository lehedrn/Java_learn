package com.coderlee.designpattern.behavioral.chain;

/**
 * 订单类型枚举
 */
public enum OrderType {
    /**
     * 普通订单
     */
    NORMAL,

    /**
     * 团购订单
     */
    GROUP_BUY,

    /**
     * 预售订单
     */
    PRE_SALE,

    /**
     * 秒杀订单
     */
    FLASH_SALE,

    /**
     * 海外购订单
     */
    OVERSEAS
}
