package com.coderlee.guarded.suspension.lock.alarm.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报警信息实体类
 * <p>
 * 包含报警的基本信息，如服务器编号、IP地址、MAC地址和报警类型。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmInfo {
    /**
     * 服务器编号
     */
    private Integer serverNo;
    /**
     * IP地址
     */
    private String ip;
    /**
     * MAC地址
     */
    private String mac;
    /**
     * 报警类型
     */
    private String alarmType;
}
