package com.coderlee.concurrent.learn.lab01;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>线程处理测试类，用于演示和测试各种线程操作场景。</p>
 * <p>该类包含多个静态方法，每个方法都展示了一种特定的线程操作或行为，
 * 例如线程创建、启动、中断、线程组管理等。这些方法可以独立运行，便于学习和理解Java中的线程机制。</p>
 *
 * @author coderlee
 * @version 1.0
 */
@Slf4j
public class ThreadHandlerTest {

    /**
     * 程序入口方法，调用其他静态方法以执行不同的线程操作。
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 调用main16方法进行线程组批量中断测试
        main16();
    }

    /**
     * 创建并启动一个命名线程，并打印主线程和子线程的名称。
     */
    public static void main1() {
        // 创建一个名为"thread-lee-001"的线程
        Thread thread = new Thread(() -> {
            log.info("子线程名称===>> [{}]", Thread.currentThread().getName());
        }, "thread-lee-001");
        thread.start(); // 启动子线程
        log.info("主线程名称===>> " + Thread.currentThread().getName()); // 打印主线程名称
    }

    /**
     * 创建一个线程并在启动前设置其名称，然后打印主线程和子线程的名称。
     */
    public static void main2() {
        Thread thread = new Thread(() -> {
            log.info("子线程名称===>> [{}]", Thread.currentThread().getName());
        });
        thread.setName("thread-lee-002"); // 设置线程名称
        thread.start(); // 启动子线程
        log.info("主线程名称===>> " + Thread.currentThread().getName()); // 打印主线程名称
    }

    /**
     * 创建一个属于指定线程组的线程，并打印主线程、子线程以及子线程所属线程组的名称。
     */
    public static void main3() {
        // 创建一个属于"thread-lee-group"线程组的线程
        Thread thread = new Thread(new ThreadGroup("thread-lee-group"), () -> {
            log.info("子线程名称===>> [{}]", Thread.currentThread().getName());
        });
        thread.start(); // 启动子线程
        log.info("主线程名称===>> [{}]", Thread.currentThread().getName()); // 打印主线程名称
        log.info("子线程所在的线程组名称为===>>>  [{}]", thread.getThreadGroup().getName()); // 打印线程组名称
    }

    /**
     * 创建一个线程，在线程中休眠2秒，并打印时间戳和线程名称。
     */
    public static void main4() {
        Thread thread = new Thread(() -> {
            log.info("当前时间为===>>> [{}]", LocalDateTime.now()); // 打印开始时间
            log.info("子线程名称===>>  [{}]", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(2); // 休眠2秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("当前时间为===>>> [{}]", LocalDateTime.now()); // 打印结束时间
        }, "thread-lee-004");
        thread.start(); // 启动子线程
        log.info("主线程名称===>> [{}]", Thread.currentThread().getName()); // 打印主线程名称
    }

    /**
     * 创建一个线程，测试线程中断的行为，并捕获中断异常后重新设置中断标记。
     *
     * @throws InterruptedException 如果线程在休眠时被中断
     */
    public static void main5() throws InterruptedException {
        Thread thread = new Thread(() -> {
            log.info("子线程名称===>> [{}]", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(5); // 子线程休眠5秒
            } catch (InterruptedException e) {
                log.info("中断休眠中的线程会抛出异常，并清空中断标记，捕获异常后重新设置中断标记");
                Thread.currentThread().interrupt(); // 重新设置中断标记
            }
        });
        thread.setName("thread-lee-005"); // 设置线程名称
        thread.start(); // 启动子线程
        TimeUnit.MILLISECONDS.sleep(500); // 主线程短暂休眠以确保子线程已启动
        log.info("在子线程中中断");
        thread.interrupt(); // 中断子线程
        log.info("主线程名称===>> [{}]", Thread.currentThread().getName()); // 打印主线程名称
    }

    /**
     * 创建一个线程，通过轮询中断标记退出循环，测试线程中断的效果。
     *
     * @throws InterruptedException 如果主线程在休眠时被中断
     */
    public static void main6() throws InterruptedException {
        Thread thread = new Thread(() -> {
            log.info("子线程名称===>> [{}]", Thread.currentThread().getName());
            while (!Thread.currentThread().isInterrupted()) {
                // 循环直到线程被中断
            }
            log.info("子线程退出了while循环");
        });
        thread.setName("thread-lee-006"); // 设置线程名称
        thread.start(); // 启动子线程
        TimeUnit.MILLISECONDS.sleep(500); // 主线程短暂休眠以确保子线程已启动
        log.info("在主线程中中断子线程");
        thread.interrupt(); // 中断子线程
        log.info("主线程名称===>> [{}]", Thread.currentThread().getName()); // 打印主线程名称
    }

    /**
     * 测试线程间的等待/通知机制，使用同步块和`wait`/`notify`方法。
     *
     * @throws InterruptedException 如果主线程在休眠时被中断
     */
    public static void main7() throws InterruptedException {
        final Object obj = new Object(); // 用于同步的对象
        Thread thread = new Thread(() -> {
            log.info("子线程名称===>> [{}]", Thread.currentThread().getName());
            log.info("子线程等待");
            synchronized (obj) {
                try {
                    obj.wait(); // 子线程进入等待状态
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info("子线程被唤醒");
        });
        thread.setName("thread-lee-007"); // 设置线程名称
        thread.start(); // 启动子线程
        TimeUnit.MILLISECONDS.sleep(500); // 主线程短暂休眠以确保子线程已启动
        log.info("主线程通知子线程");
        synchronized (obj) {
            obj.notify(); // 主线程通知子线程继续执行
        }
        log.info("主线程名称===>> [{}]", Thread.currentThread().getName()); // 打印主线程名称
    }

    /**
     * 使用已过时的`suspend`和`resume`方法测试线程挂起与恢复的行为。
     *
     * @throws InterruptedException 如果主线程在休眠时被中断
     */
    @SuppressWarnings("deprecation")
    public static void main8() throws InterruptedException {
        final Object obj = new Object(); // 用于同步的对象
        Thread thread = new Thread(() -> {
            log.info("子线程名称===>> [{}]", Thread.currentThread().getName());
            synchronized (obj) {
                log.info("子线程===>> [{}] 被挂起", Thread.currentThread().getName());
                Thread.currentThread().suspend(); // 挂起子线程（已过时）
            }
            log.info("子线程===>> [{}] 被唤醒", Thread.currentThread().getName());
        });
        thread.setName("thread-lee-008"); // 设置线程名称
        thread.start(); // 启动子线程
        TimeUnit.MILLISECONDS.sleep(500); // 主线程短暂休眠以确保子线程已启动
        log.info("主线程[{}] 通知子线程===>> [{}] 继续执行", Thread.currentThread().getName(), thread.getName());
        thread.resume(); // 恢复子线程（已过时）
        log.info("主线程名称===>> [{}]", Thread.currentThread().getName()); // 打印主线程名称
    }

    private static int sum = 0;

    /**
     * 测试线程间的协作，使用`join`方法等待子线程完成任务。
     *
     * @throws InterruptedException 如果主线程在等待子线程时被中断
     */
    public static void main9() throws InterruptedException {
        Thread thread = new Thread(() -> {
            log.info("子线程名称===>> [{}]", Thread.currentThread().getName());
            IntStream.range(0, 1000).forEach((i) -> {
                sum += 1; // 子线程对共享变量sum进行累加
            });
        });
        thread.setName("thread-lee-009"); // 设置线程名称
        thread.start(); // 启动子线程
        thread.join(); // 主线程等待子线程完成
        log.info("主线程获取到的结果为===>>> [{}]", sum); // 打印累加结果
        log.info("主线程名称===>> [{}]", Thread.currentThread().getName()); // 打印主线程名称
    }

    /**
     * 使用已过时的`stop`方法强制终止线程，测试线程强制退出的行为。
     *
     * @throws InterruptedException 如果主线程在休眠时被中断
     */
    @SuppressWarnings("deprecation")
    public static void main10() throws InterruptedException {
        Thread thread = new Thread(() -> {
            log.info("子线程名称===>> [{}]", Thread.currentThread().getName());
            while (true) {
                // 子线程进入无限循环
            }
        }, "thread-lee-010");
        log.info("启动子线程[{}]===>> [{}]", thread.getName(), LocalDateTime.now()); // 打印启动时间
        thread.start(); // 启动子线程
        TimeUnit.SECONDS.sleep(5); // 主线程休眠5秒
        log.info("强制退出子线程[{}]===>>> [{}]", thread.getName(), LocalDateTime.now()); // 打印终止时间
        thread.stop(); // 强制终止子线程（已过时）
        log.info("主线程名称===>> [{}]", Thread.currentThread().getName()); // 打印主线程名称
    }

    /**
     * 测试线程组的功能，统计线程组中活跃的线程数和线程组数。
     *
     * @throws InterruptedException 如果主线程在休眠时被中断
     */
    public static void main11() throws InterruptedException {
        ThreadGroup threadGroup = new ThreadGroup("thread-lee-group"); // 创建线程组
        ThreadGroup subThreadGroup = new ThreadGroup(threadGroup, "sub-thread-lee-group"); // 创建子线程组
        Thread thread1 = new Thread(threadGroup, () -> {
            log.info("子线程名称===>> [{}]", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1); // 子线程休眠1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(subThreadGroup, () -> {
            log.info("子线程名称===>> [{}]", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1); // 子线程休眠1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start(); // 启动子线程1
        thread2.start(); // 启动子线程2
        TimeUnit.MILLISECONDS.sleep(500); // 主线程短暂休眠以确保子线程已启动
        log.info("线程组中活跃的线程组数量为===>> [{}]", threadGroup.activeGroupCount()); // 打印活跃线程组数量
        log.info("线程组中活跃的线程数量为===>> [{}]", threadGroup.activeCount()); // 打印活跃线程数量
        log.info("主线程名称===>> [{}]", Thread.currentThread().getName()); // 打印主线程名称
    }

    /**
     * 进一步测试线程组的功能，包括嵌套线程组的统计。
     *
     * @throws InterruptedException 如果主线程在休眠时被中断
     */
    @SuppressWarnings("unused")
    public static void main12() throws InterruptedException {
        ThreadGroup threadGroup = new ThreadGroup("thread-lee-group"); // 创建线程组
        ThreadGroup subThreadGroup = new ThreadGroup(threadGroup, "sub-thread-lee-group"); // 创建子线程组
        Thread thread1 = new Thread(threadGroup, () -> {
            log.info("子线程名称===>> [{}]", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1); // 子线程休眠1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(subThreadGroup, () -> {
            log.info("子线程名称===>> [{}]", Thread.currentThread().getName());
            ThreadGroup thread2Group = new ThreadGroup("thread2-lee-group"); // 创建新的线程组
            try {
                TimeUnit.SECONDS.sleep(1); // 子线程休眠1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start(); // 启动子线程1
        thread2.start(); // 启动子线程2
        TimeUnit.MILLISECONDS.sleep(500); // 主线程短暂休眠以确保子线程已启动
        log.info("threadGroup线程组中活跃的线程组数量为===>> [{}]", threadGroup.activeGroupCount()); // 打印活跃线程组数量
        log.info("threadGroup线程组中活跃的线程数量为===>> [{}]", threadGroup.activeCount()); // 打印活跃线程数量
        log.info("subThreadGroup线程组中活跃的线程组数量为===>> [{}]", subThreadGroup.activeGroupCount()); // 打印活跃线程组数量
        log.info("主线程名称===>> [{}]", Thread.currentThread().getName()); // 打印主线程名称
    }

    /**
     * 打印当前线程所属线程组及其父线程组的名称。
     */
    public static void main13() {
        log.info(Thread.currentThread().getThreadGroup().getName()); // 打印当前线程组名称
        log.info(Thread.currentThread().getThreadGroup().getParent().getName()); // 打印父线程组名称
        log.info(Thread.currentThread().getThreadGroup().getParent().getParent().getName()); // 打印祖父线程组名称
    }

    /**
     * 测试线程组中活跃线程组数量的统计功能。
     */
    public static void main14() {
        ThreadGroup threadGroup = new ThreadGroup("thread-lee-group"); // 创建线程组
        log.info("threadGroup线程组中活跃的线程组数量为===>> [{}]", threadGroup.activeGroupCount()); // 打印活跃线程组数量
        ThreadGroup subThreadGroup = new ThreadGroup(threadGroup, "sub-thread-lee-group"); // 创建子线程组
        log.info("subThreadGroup线程组中活跃的线程组数量为===>> [{}]", subThreadGroup.activeGroupCount()); // 打印活跃线程组数量
    }

    /**
     * 使用递归和非递归方式获取线程组中的所有子线程组。
     */
    @SuppressWarnings("unused")
    public static void main15() {
        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup(); // 获取当前线程组
        ThreadGroup threadGroup = new ThreadGroup(mainGroup, "threadGroup"); // 创建线程组
        ThreadGroup subThreadGroup1 = new ThreadGroup(threadGroup, "subThreadGroup1"); // 创建子线程组1
        ThreadGroup subThreadGroup2 = new ThreadGroup(threadGroup, "subThreadGroup2"); // 创建子线程组2

        ThreadGroup[] threadGroups1 = new ThreadGroup[mainGroup.activeGroupCount()];
        mainGroup.enumerate(threadGroups1, true); // 递归获取所有子线程组
        Stream.of(threadGroups1).forEach((tg) -> {
            if (tg != null) {
                log.info("递归获取到的线程组===>> [{}]", tg.getName()); // 打印线程组名称
            }
        });

        ThreadGroup[] threadGroups2 = new ThreadGroup[mainGroup.activeGroupCount()];
        mainGroup.enumerate(threadGroups2, false); // 非递归获取直接子线程组
        Stream.of(threadGroups2).forEach((tg) -> {
            if (tg != null) {
                log.info("非递归获取到的线程组===>> [{}]", tg.getName()); // 打印线程组名称
            }
        });
    }

    /**
     * 使用线程组批量中断线程，测试线程组的中断功能。
     */
    public static void main16() {
        ThreadGroup threadGroup = new ThreadGroup("threadGroup"); // 创建线程组
        log.info("创建并启动所有的线程===>>> [{}]", LocalDateTime.now()); // 打印启动时间
        IntStream.range(0, 5).forEach((i) -> {
            new Thread(threadGroup, () -> {
                while (!Thread.currentThread().isInterrupted()) {
                    // 循环直到线程被中断
                }
                log.info("子线程[{}]被中断===>>> [{}]", Thread.currentThread().getName(), LocalDateTime.now()); // 打印中断时间
            }).start(); // 启动子线程
        });
        try {
            TimeUnit.SECONDS.sleep(5); // 主线程休眠5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("主线程中断子线程");
        threadGroup.interrupt(); // 批量中断线程组中的所有线程
    }
}