package com.coderlee.juc;

/**
 * 线程八锁示例
 * 
 * 本类通过多个场景（handler1 到 handler8）展示了 Java 多线程中锁的行为，
 * 特别是非静态同步方法和静态同步方法在不同情况下的表现。
 * 
 * 核心知识点：
 * 1. 非静态同步方法的锁默认为 `this`（当前对象实例）。
 * 2. 静态同步方法的锁为对应类的 `Class` 对象。
 * 3. 某一时刻内，只能有一个线程持有锁，无论涉及几个方法。
 */
public class Thread8MonitorDemo {

    public static void main(String[] args) {
        // 调用不同的 handler 方法来测试多线程锁的行为
        // handler1();
        // handler2();
        // handler3();
        // handler4();
        handler5();
        // handler6();
        // handler7();
        // handler8();
    }

    /**
     * 场景 1：两个普通同步方法，两个线程，共用一个对象实例
     * 打印结果：one two
     * 原因：两个线程竞争同一把锁（this），按顺序执行。
     */
    public static void handler1() {
        NumberDemoV1 numberDemo = new NumberDemoV1();
        new Thread(() -> numberDemo.getOne()).start(); // 线程 1 调用 getOne()
        new Thread(() -> numberDemo.getTwo()).start(); // 线程 2 调用 getTwo()
    }

    /**
     * 场景 2：新增 Thread.sleep() 给 getOne()，两个线程，共用一个对象实例
     * 打印结果：one two
     * 原因：虽然 getOne() 延迟了，但锁仍然是 this，按顺序执行。
     */
    public static void handler2() {
        NumberDemoV2 numberDemo = new NumberDemoV2();
        new Thread(() -> numberDemo.getOne()).start(); // 线程 1 调用 getOne()
        new Thread(() -> numberDemo.getTwo()).start(); // 线程 2 调用 getTwo()
    }

    /**
     * 场景 3：新增普通方法 getThree()，三个线程，共用一个对象实例
     * 打印结果：three one two
     * 原因：getThree() 是普通方法，不受锁限制，直接执行。
     */
    public static void handler3() {
        NumberDemoV3 numberDemo = new NumberDemoV3();
        new Thread(() -> numberDemo.getOne()).start();   // 线程 1 调用 getOne()
        new Thread(() -> numberDemo.getTwo()).start();   // 线程 2 调用 getTwo()
        new Thread(() -> numberDemo.getThree()).start(); // 线程 3 调用 getThree()
    }

    /**
     * 场景 4：两个普通同步方法，两个线程，使用两个不同的对象实例
     * 打印结果：two one
     * 原因：每个对象有自己的锁，互不影响。
     */
    public static void handler4() {
        NumberDemoV4 numberDemo1 = new NumberDemoV4();
        NumberDemoV4 numberDemo2 = new NumberDemoV4();
        new Thread(() -> numberDemo1.getOne()).start(); // 线程 1 调用 getOne()
        new Thread(() -> numberDemo2.getTwo()).start(); // 线程 2 调用 getTwo()
    }

    /**
     * 场景 5：getOne() 改为静态同步方法且 sleep 3s，两个线程，共用一个对象实例
     * 打印结果：two one
     * 原因：静态同步方法的锁是类对象，普通同步方法的锁是实例对象，两者互不影响。
     */
    @SuppressWarnings("static-access")
    public static void handler5() {
        NumberDemoV5 numberDemo = new NumberDemoV5();
        new Thread(() -> numberDemo.getOne()).start(); // 线程 1 调用静态方法 getOne()
        new Thread(() -> numberDemo.getTwo()).start();   // 线程 2 调用普通方法 getTwo()
    }

    /**
     * 场景 6：两个方法均为静态同步方法，两个线程，共用一个类对象
     * 打印结果：one two
     * 原因：静态同步方法的锁是类对象，两个线程竞争同一把锁。
     */
    public static void handler6() {
        new Thread(() -> NumberDemoV6.getOne()).start(); // 线程 1 调用静态方法 getOne()
        new Thread(() -> NumberDemoV6.getTwo()).start(); // 线程 2 调用静态方法 getTwo()
    }

    /**
     * 场景 7：一个静态同步方法，一个非静态同步方法，两个线程，使用两个对象实例
     * 打印结果：two one
     * 原因：静态同步方法的锁是类对象，普通同步方法的锁是实例对象，两者互不影响。
     */
    @SuppressWarnings("static-access")
    public static void handler7() {
        NumberDemoV7 numberDemo1 = new NumberDemoV7();
        NumberDemoV7 numberDemo2 = new NumberDemoV7();
        new Thread(() -> numberDemo1.getOne()).start(); // 线程 1 调用静态方法 getOne()
        new Thread(() -> numberDemo2.getTwo()).start(); // 线程 2 调用普通方法 getTwo()
    }

    /**
     * 场景 8：两个静态同步方法，两个线程，使用两个对象实例
     * 打印结果：one two
     * 原因：静态同步方法的锁是类对象，即使对象实例不同，锁仍然相同。
     */
    @SuppressWarnings("static-access")
    public static void handler8() {
        NumberDemoV8 numberDemo1 = new NumberDemoV8();
        NumberDemoV8 numberDemo2 = new NumberDemoV8();
        new Thread(() -> numberDemo1.getOne()).start(); // 线程 1 调用静态方法 getOne()
        new Thread(() -> numberDemo2.getTwo()).start(); // 线程 2 调用静态方法 getTwo()
    }
}

/**
 * 场景 1 的测试类
 * 两个普通同步方法，锁为 this。
 */
class NumberDemoV1 {
    public synchronized void getOne() {
        System.out.println("one"); // 输出 "one"
    }

    public synchronized void getTwo() {
        System.out.println("two"); // 输出 "two"
    }
}

/**
 * 场景 2 的测试类
 * 新增 Thread.sleep() 给 getOne()，锁为 this。
 */
class NumberDemoV2 {
    public synchronized void getOne() {
        try {
            Thread.sleep(3000L); // 延迟 3 秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one"); // 输出 "one"
    }

    public synchronized void getTwo() {
        System.out.println("two"); // 输出 "two"
    }
}

/**
 * 场景 3 的测试类
 * 新增普通方法 getThree()，不受锁限制。
 */
class NumberDemoV3 {
    public synchronized void getOne() {
        try {
            Thread.sleep(300L); // 延迟 0.3 秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one"); // 输出 "one"
    }

    public synchronized void getTwo() {
        System.out.println("two"); // 输出 "two"
    }

    public void getThree() {
        System.out.println("three"); // 输出 "three"
    }
}

/**
 * 场景 4 的测试类
 * 两个普通同步方法，使用两个对象实例，锁为 this。
 */
class NumberDemoV4 {
    public synchronized void getOne() {
        try {
            Thread.sleep(300L); // 延迟 0.3 秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one"); // 输出 "one"
    }

    public synchronized void getTwo() {
        System.out.println("two"); // 输出 "two"
    }
}

/**
 * 场景 5 的测试类
 * getOne() 为静态同步方法，getTwo() 为普通同步方法。
 */
class NumberDemoV5 {
    public static synchronized void getOne() {
        try {
            Thread.sleep(300L); // 延迟 0.3 秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one"); // 输出 "one"
    }

    public synchronized void getTwo() {
        System.out.println("two"); // 输出 "two"
    }
}

/**
 * 场景 6 的测试类
 * 两个静态同步方法，锁为类对象。
 */
class NumberDemoV6 {
    public static synchronized void getOne() {
        try {
            Thread.sleep(300L); // 延迟 0.3 秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one"); // 输出 "one"
    }

    public static synchronized void getTwo() {
        System.out.println("two"); // 输出 "two"
    }
}

/**
 * 场景 7 的测试类
 * 一个静态同步方法，一个非静态同步方法。
 */
class NumberDemoV7 {
    public static synchronized void getOne() {
        try {
            Thread.sleep(300L); // 延迟 0.3 秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one"); // 输出 "one"
    }

    public synchronized void getTwo() {
        System.out.println("two"); // 输出 "two"
    }
}

/**
 * 场景 8 的测试类
 * 两个静态同步方法，锁为类对象。
 */
class NumberDemoV8 {
    public static synchronized void getOne() {
        try {
            Thread.sleep(300L); // 延迟 0.3 秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one"); // 输出 "one"
    }

    public static synchronized void getTwo() {
        System.out.println("two"); // 输出 "two"
    }
}