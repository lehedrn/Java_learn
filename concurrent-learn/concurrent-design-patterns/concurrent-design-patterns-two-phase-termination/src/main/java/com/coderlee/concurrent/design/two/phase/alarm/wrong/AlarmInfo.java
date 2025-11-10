package com.coderlee.concurrent.design.two.phase.alarm.wrong;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 报警信息实体类
 * <p>
 * 用于封装报警系统中的报警信息，包括报警ID、报警类型和附加信息
 * </p>
 *
 * @see AlarmType 报警类型枚举
 */
@Data
@AllArgsConstructor
public class AlarmInfo {
    /**
     * 报警信息唯一标识符
     */
    private String id;

    /**
     * 报警类型
     *
     * @see AlarmType
     */
    private AlarmType alarmType;

    /**
     * 报警附加信息
     */
    private String extra;
}
