package com.coderlee.concurrent.design.two.phase.alarm.right;

import com.coderlee.concurrent.design.thread.AbstractTerminationThread;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 告警发送线程类，负责处理告警信息的后台发送任务
 * 继承自AbstractTerminationThread，支持两阶段终止模式
 *
 * @see com.coderlee.concurrent.design.thread.AbstractTerminationThread
 */
@Slf4j
public class AlarmSendingThread extends AbstractTerminationThread {
    /**
     * 告警信息队列，用于存储待处理的告警信息
     */
    private final BlockingQueue<AlarmInfo> alarmQueues;

    /**
     * 已提交告警信息注册表，用于记录告警信息的提交次数
     * key为告警信息的唯一标识符，value为提交次数计数器
     */
    private final ConcurrentMap<String, AtomicInteger> submittedAlarmRegistry;

    /**
     * 构造函数，初始化告警队列和告警注册表
     */
    public AlarmSendingThread() {
        this.alarmQueues = new ArrayBlockingQueue<>(100);
        this.submittedAlarmRegistry = new ConcurrentHashMap<>();
    }

    /**
     * 执行告警发送任务的核心方法
     * 从告警队列中取出告警信息并执行发送操作
     *
     * @throws InterruptedException 当线程被中断时抛出
     */
    @Override
    protected void doRun() throws InterruptedException {
        AlarmInfo alarmInfo;
        alarmInfo = alarmQueues.take();
        log.info("告警线程从队列中拉取到告警信息: {}", alarmInfo);
        // 告警任务数量-1
        terminationToken.noExecuteTaskCount.decrementAndGet();
        // 发送告警信息
        try {
            doSendAlarm();
            log.info("告警信息: {} 上报完成", alarmInfo);
        } catch (Exception e) {
            log.error("发送告警信息异常", e);
        }

        // 如果是恢复告警，则清除相关的故障告警记录
        if (Objects.equals(alarmInfo.getAlarmType(), AlarmType.RESUME)) {
            log.info("告警: {} 已恢复, 清空告警次数", alarmInfo);
            submittedAlarmRegistry.remove(alarmInfo.getUniqueIdByAlarmType(AlarmType.FAULT));
            submittedAlarmRegistry.remove(alarmInfo.getUniqueIdByAlarmType(AlarmType.RESUME));
        }
    }

    /**
     * 发送告警信息
     * 检查是否为重复提交，如果不是则加入队列等待处理
     *
     * @param alarmInfo 待发送的告警信息
     * @return 重复提交次数，-1表示已终止，0表示首次提交，大于0表示重复提交次数
     */
    public int sendAlarm(AlarmInfo alarmInfo) {
        // 检查是否已经终止执行
        if (terminationToken.isToShutdown()) {
            log.error("终止执行: {}", alarmInfo);
            return -1;
        }
        try {
            // 放入到告警队列中
            AtomicInteger prevSubmittedCounter;
            prevSubmittedCounter = submittedAlarmRegistry.putIfAbsent(alarmInfo.getUniqueId(), new AtomicInteger());
            if (prevSubmittedCounter == null) {
                log.info("当前没有编号为报警: {} 的告警信息, 将其放到告警队列中", alarmInfo.getId());
                // 代表之前该类型的告警为空，未完成的任务+1
                terminationToken.noExecuteTaskCount.incrementAndGet();
                // 放入到告警队列中
                alarmQueues.put(alarmInfo);
            } else {
                log.info("编号为报警: {} 告警故障还没恢复, 不需要重复上报告警, 知识增加告警次数", alarmInfo.getId());
                // 当前的故障还没有恢复，不需要重复上报告警，只是增加告警数量的次数
                return prevSubmittedCounter.incrementAndGet();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    /**
     * 执行实际的告警发送操作
     * 这里模拟发送过程，实际应用中会调用具体的发送逻辑
     */
    private void doSendAlarm() {
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
