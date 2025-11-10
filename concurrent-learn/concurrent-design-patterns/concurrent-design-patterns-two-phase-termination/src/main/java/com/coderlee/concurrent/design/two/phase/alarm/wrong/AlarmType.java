package com.coderlee.concurrent.design.two.phase.alarm.wrong;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报警类型枚举类
 * <p>
 * 定义系统中支持的报警类型，包括故障报警和恢复报警
 * </p>
 */
@Getter
@AllArgsConstructor
public enum AlarmType {
    /**
     * 故障报警类型
     */
    FAULT(1, "故障"),

    /**
     * 恢复/刷新报警类型
     */
    RESUME(2, "刷新");

    /**
     * 报警类型的数值标识
     */
    private Integer alarmType;

    /**
     * 报警类型的描述信息
     */
    private String desc;
}
