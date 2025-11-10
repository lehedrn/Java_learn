package com.coderlee.guarded.suspension.lock.alarm.agent;

import com.coderlee.guarded.suspension.lock.alarm.action.GuardedAction;
import com.coderlee.guarded.suspension.lock.alarm.blocker.Blocker;
import com.coderlee.guarded.suspension.lock.alarm.blocker.JdkConditionBlocker;
import com.coderlee.guarded.suspension.lock.alarm.info.AlarmInfo;
import com.coderlee.guarded.suspension.lock.alarm.predicate.Predicate;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 报警代理类
 * <p>
 * 负责管理与报警服务器的连接状态，并在连接可用时发送报警信息。
 * 使用Guarded Suspension模式确保只有在连接建立后才发送报警。
 *
 * @see com.coderlee.guarded.suspension.lock.alarm.blocker.Blocker
 * @see com.coderlee.guarded.suspension.lock.alarm.predicate.Predicate
 */
@Slf4j
public class AlarmAgent {

    /**
     * 与报警服务器的连接状态标志
     */
    private volatile boolean connectedToServer = false;

    /**
     * 判断报警代理是否已连接的谓词条件
     */
    private Predicate agentConnected = () -> connectedToServer;

    /**
     * 阻塞器，用于控制访问权限
     */
    private Blocker blocker = new JdkConditionBlocker(false);

    /**
     * 发送报警信息
     * <p>
     * 如果当前未连接到报警服务器，则会阻塞等待直到连接建立。
     *
     * @param alarmInfo 报警信息
     * @throws Exception 执行过程中可能抛出的异常
     * @see com.coderlee.guarded.suspension.lock.alarm.blocker.Blocker#callWithGuard(com.coderlee.guarded.suspension.lock.alarm.action.GuardedAction)
     */
    public void sendAlarm(AlarmInfo alarmInfo) throws Exception {
        // 创建受保护的动作
        GuardedAction<Boolean> guardedAction = new GuardedAction<Boolean>(agentConnected){
            @Override
            public Boolean call() throws Exception {
                // 执行目标函数
                try{
                    doSendAlarm(alarmInfo);
                    // 执行成功，返回true
                    return Boolean.TRUE;
                }catch (Exception e){
                    // 执行失败，返回false
                    return Boolean.FALSE;
                }
            }
        };
        // 通过blocker执行目标
        blocker.callWithGuard(guardedAction);
    }

    /**
     * 实际执行发送报警信息的操作
     *
     * @param alarmInfo 报警信息
     */
    private void doSendAlarm(AlarmInfo alarmInfo) {
        log.info("发送报警信息开始: {}", alarmInfo);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("发送报警信息结束: {}", alarmInfo);
    }

    /**
     * 初始化报警代理
     * <p>
     * 启动连接任务和心跳检测任务。
     */
    public void initAlarmAgent() {
        // 连接报警服务器的线程
        Thread connectingThread = new Thread(new ConnectingTask());
        connectingThread.start();

        // 每隔5s发送一次心跳到报警服务器
        ScheduledThreadPoolExecutor heartbeatExecutor = new ScheduledThreadPoolExecutor(5, new ThreadFactory() {

            private AtomicInteger index = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread("heartbeat-thread-" + index);
                // 当jvm退出的时候退出
                thread.setDaemon(true);
                return thread;
            }
        });
        // 每5s执行一次
        heartbeatExecutor.scheduleAtFixedRate(new HeartbeatTask(), 5000, 5000, TimeUnit.MILLISECONDS);
    }


    /**
     * 与报警服务器建立连接的任务
     */
    private class ConnectingTask implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("报警连接建立成功...");
            onConnected();
        }
    }

    /**
     * 心跳检查任务
     */
    private class HeartbeatTask implements Runnable {
        @Override
        public void run() {
            if (!testConnection()) {
                // 断开连接
                onDisconnected();
                // 重新建立连接
                reconnected();
            }
        }
    }

    /**
     * 检测连接是否正常
     *
     * @return 连接是否正常的布尔值
     */
    private boolean testConnection() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("检测连接正常...");
        return true;
    }

    /**
     * 重新连接
     */
    private void reconnected() {
        // 重新建立连接
        ConnectingTask connectingTask = new ConnectingTask();
        // 直接通过心跳线程执行一次重新连接服务器的操作
        connectingTask.run();
    }

    /**
     * 连接断开处理
     */
    private void onDisconnected() {
        // 将connectedToServer设置为false，表示断开连接
        connectedToServer = false;
    }

    /**
     * 确认和报警服务器建立连接后的处理
     */
    private void onConnected() {
        // 通过blocker去唤醒
        try {
            blocker.signalAfter(() -> {
                log.info("更新connectedToServer变量的值为true...");
                connectedToServer = true;
                // 满足条件，唤醒被阻塞的线程
                return Boolean.TRUE;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
