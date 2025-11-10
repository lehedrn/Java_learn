package com.coderlee.guarded.suspension.lock.alarm.test;

import com.coderlee.guarded.suspension.lock.alarm.agent.AlarmAgent;
import com.coderlee.guarded.suspension.lock.alarm.info.AlarmInfo;

public class AlarmAgentTest {
    public static void main(String[] args) {
        AlarmAgent alarmAgent = new AlarmAgent();
        alarmAgent.initAlarmAgent();

        AlarmInfo alarmInfo = new AlarmInfo(1001, "127.0.0.1", "xx:xx:xx:xx", "alarm");
        try {
            alarmAgent.sendAlarm(alarmInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
