/**
 * 告警管理器，负责告警信息的统一管理和发送
 * 采用单例模式，确保系统中只有一个告警管理器实例
 */
package com.coderlee.concurrent.design.two.phase.alarm.right;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmManager {
    /**
     * 标识是否已请求关闭
     */
    private volatile boolean shutdownRequested = false;

    /**
     * 单例实例
     */
    private static final AlarmManager INSTANCE = new AlarmManager();

    /**
     * 告警发送线程实例
     */
    private final AlarmSendingThread alarmSendingThread;

    /**
     * 私有构造函数，创建告警发送线程
     */
    public AlarmManager() {
        log.info("创建上报告警信息的后台线程");
        alarmSendingThread = new AlarmSendingThread();
    }

    /**
     * 获取告警管理器单例实例
     *
     * @return 告警管理器实例
     */
    public static AlarmManager getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化告警管理器，启动告警发送线程
     */
    public void init() {
        alarmSendingThread.start();
    }

    /**
     * 发送告警信息
     *
     * @param id 告警ID
     * @param alarmType 告警类型
     * @param extra 额外信息
     * @return 重复提交次数
     */
    public int sendAlarm(String id, AlarmType alarmType, String extra) {
        AlarmInfo alarmInfo = new AlarmInfo(id, alarmType, extra);
        // 重复提交的数量
        int duplicateSubmissionCount = 0;
        try {
            duplicateSubmissionCount = alarmSendingThread.sendAlarm(alarmInfo);
        } catch (Exception e) {
            log.error("发送告警信息异常", e);
        }
        return duplicateSubmissionCount;
    }

    /**
     * 关闭告警管理器，终止告警发送线程
     * 使用synchronized确保线程安全
     *
     * @throws IllegalStateException 当已经调用过shutdown方法时抛出
     */
    public synchronized void shutdown() {
        if (shutdownRequested) {
            throw new IllegalStateException("已经调用了shutdown方法....");
        }
        // 关闭告警后台线程
        alarmSendingThread.terminate();
        shutdownRequested = true;
    }
}
