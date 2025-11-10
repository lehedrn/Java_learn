package com.coderlee.concurrent.design.two.phase.alarm.right;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 告警管理器测试类，用于验证告警管理器的功能
 */
@Slf4j
public class AlarmManagerTest {

    public static void main(String[] args) {
        // 获取告警管理器实例并初始化
        AlarmManager alarmManager = AlarmManager.getInstance();
        alarmManager.init();

        // 启动线程发送第一个告警
        new Thread(() -> {
            int duplicateAlarmNumber = alarmManager.sendAlarm("001", AlarmType.FAULT, "001告警信息");
            log.info("发送报警001完成, 001重复提交次数: {}", duplicateAlarmNumber);
        }).start();

        // 启动线程发送第二个告警
        new Thread(() -> {
            int duplicateAlarmNumber = alarmManager.sendAlarm("002", AlarmType.FAULT, "002告警信息");
            log.info("发送报警002完成, 002重复提交次数: {}", duplicateAlarmNumber);
        }).start();

        // 启动线程发送与第二个告警相同的告警，测试重复提交处理
        new Thread(() -> {
            int duplicateAlarmNumber = alarmManager.sendAlarm("002", AlarmType.FAULT, "002告警信息");
            log.info("发送报警002完成, 002重复提交次数: {}", duplicateAlarmNumber);
        }).start();

        try {
            // 等待一段时间让告警处理完成
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 启动线程关闭告警管理器
        new Thread(alarmManager::shutdown).start();
    }
}
