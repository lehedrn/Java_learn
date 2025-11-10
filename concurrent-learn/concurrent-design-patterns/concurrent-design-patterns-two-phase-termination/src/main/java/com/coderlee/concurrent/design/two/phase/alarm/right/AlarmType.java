package com.coderlee.concurrent.design.two.phase.alarm.right;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 告警类型枚举，定义了系统支持的告警类型
 */
@Getter
@AllArgsConstructor
public enum AlarmType {
    /**
     * 故障告警类型
     */
    FAULT(1, "故障"),

    /**
     * 恢复告警类型
     */
    RESUME(2, "刷新");

    /**
     * 告警类型的数值标识
     */
    private Integer alarmType;

    /**
     * 告警类型的描述信息
     */
    private String desc;
}
