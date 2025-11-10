package com.coderlee.concurrent.design.two.phase.alarm.right;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 告警信息类，用于封装告警的基本信息
 *
 * @see AlarmType 告警类型枚举
 */
@Data
@AllArgsConstructor
public class AlarmInfo {
    /**
     * 告警ID
     */
    private String id;

    /**
     * 告警类型
     *
     * @see AlarmType
     */
    private AlarmType alarmType;

    /**
     * 额外信息
     */
    private String extra;

    /**
     * 获取告警信息的唯一标识符
     *
     * @return 唯一标识符，格式为"告警类型描述:ID:额外信息"
     */
    public String getUniqueId() {
        return this.alarmType.getDesc() + ":" + this.id + ":" + this.extra;
    }

    /**
     * 根据指定告警类型获取唯一标识符
     *
     * @param alarmType 指定的告警类型
     * @return 唯一标识符，格式为"告警类型描述:ID:额外信息"
     */
    public String getUniqueIdByAlarmType(AlarmType alarmType) {
        return alarmType.getDesc() + ":" + this.id + ":" + this.getExtra();
    }
}
