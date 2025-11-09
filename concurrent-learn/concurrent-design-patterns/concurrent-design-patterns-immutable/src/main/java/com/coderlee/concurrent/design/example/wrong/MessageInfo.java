package com.coderlee.concurrent.design.example.wrong;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 消息实体类
 */
@Data
@AllArgsConstructor
public class MessageInfo {
    /**
     * 设备编号
     */
    private String deviceCode;
    /**
     * 第三方消息推送的接口
     */
    private String messageUrl;
}
