package com.coderlee.concurrent.design.promise.common.domain;

import lombok.Data;

/**
 * 积分实体类
 * 该类表示一个积分对象，包含积分的状态信息。
 * @author coderlee
 */
@Data
public class Integral {
    /**
     * 积分状态，默认为false（未激活）
     * true表示已发送或已激活，false表示未发送或未激活
     */
    private boolean status = false;
}
