package com.coderlee.juc1.locks;

import com.coderlee.juc1.utils.SleepUtils;
import lombok.extern.slf4j.Slf4j;

/// Lock8Demo 演示了 Java 中 synchronized 关键字的8种场景
/// 包含多个演示场景，展示了：
/// - 静态同步方法与实例同步方法的区别
/// - 不同对象实例间的锁竞争关系
/// - 同步方法与普通方法的执行顺序
/// - 多线程环境下锁的作用范围和机制
/// 1. demo1与demo2:
///   一个对象里面如果有多个Synchronized方法，某一个时刻内，只要有一个线程去调用其中的一个Synchronized方法了，其他的线程只能等待。
///   换句话说，某一个时刻内，只能有唯一的一个线程去访问这些Synchronized方法，锁的是当前对象this，被锁后，其他的线程不能进入到当前对象的其他的Synchronized方法。
/// 2. demo3与demo4:
///   加个普通方法后发现和同步锁无关
///   换个两个对象后，不是同一把锁了，情况立刻变化
/// 3. demo5与demo6:
///   都换成静态同步方法后，情况又变化了
///   三种Synchronized锁的内容有一些差别:
///   对于普通同步方法，锁的是当前实例对象，通常指this，具体的一部手机，所有的普通同步方法用的都是同一把锁，即实例对象本身。
///   对于静态同步方法，说的是当前类的Class对象，如Phone.class唯一的一个模板
///   对于同步方法，锁的是Synchronized括号内的对象
/// 4. demo7与demo8:
///   当一个线程试图访问同步代码时它首先必须得到锁，正常退出或抛异常时必须释放锁。
///   所有的普通同步方法用的都是同一把锁，即实例对象本身，就是new出来的具体示例对象本身，this
///   也就说如果一个实例对象的普通同步方法获取锁后，该实例对象的其他普通同步方法必须等待获取锁的方法释放后才能获取锁。
///   所有的静态同步方法用的也是同一把锁，即类对象本身，就是唯一模板Class
///   具体实例对象this和唯一模板Class，这两把锁是两个不同的对象，所以静态同步方法与普通同步方法之间是不会有竞态条件的
///   但是一旦一个静态同步方法获取锁后，其他的静态同步方法都必须等待该方法释放锁后才能获取锁。
public class Lock8Demo {

    public static void main(String[] args) {
//        demo1();   // 场景1：标准访问，同一对象的两个同步方法
//        demo2();   // 场景2：同步方法中有延迟，验证锁的互斥性
//        demo3();   // 场景3：同步方法与普通方法的执行顺序
//        demo4();   // 场景4：不同对象实例的同步方法
//        demo5();   // 场景5：静态同步方法，同一部手机
//        demo6();   // 场景6：静态同步方法，两部手机
//        demo7();   // 场景7：静态同步方法与实例同步方法，一部手机
        demo8();     // 场景8：静态同步方法与实例同步方法，两部手机
    }

    /**
     * 场景8: 有一个静态同步方法，一个普通同步方法，两部手机
     * 结果：短信先打印
     * 原因：静态同步方法锁定的是Class对象，普通同步方法锁定的是实例对象，两者不冲突
     */
    public static void demo8() {
        // 创建第一部手机对象
        Phone7 phone1 = new Phone7();
        // 创建线程A执行静态同步方法sendEmail()
        Thread t1 = new Thread(() -> phone1.sendEmail(), "a");
        t1.start();
        
        // 主线程休眠200毫秒，确保t1先启动
        SleepUtils.sleep(200);
        
        // 创建第二部手机对象
        Phone7 phone2 = new Phone7();
        // 创建线程B执行实例同步方法sendSMS()
        Thread t2 = new Thread(phone2::sendSMS, "b");
        t2.start();
    }

    /**
     * 场景7: 有一个静态同步方法，一个普通同步方法，一部手机
     * 结果：短信先打印
     * 原因：静态同步方法锁定的是Class对象，普通同步方法锁定的是实例对象，两者不冲突
     */
    public static void demo7() {
        // 创建手机对象
        Phone7 phone = new Phone7();
        // 创建线程A执行静态同步方法sendEmail()
        Thread t1 = new Thread(() -> phone.sendEmail(), "a");
        t1.start();
        
        // 主线程休眠200毫秒，确保t1先启动
        SleepUtils.sleep(200);
        
        // 创建线程B执行实例同步方法sendSMS()
        Thread t2 = new Thread(() -> phone.sendSMS(), "b");
        t2.start();
    }

    /**
     * 场景6: 两个静态同步方法，两部手机
     * 结果：邮件先打印
     * 原因：静态同步方法锁定的是Class对象，所有实例共享同一个Class锁
     */
    public static void demo6() {
        // 创建两部手机对象
        Phone5 phone1 = new Phone5();
        Phone5 phone2 = new Phone5();
        
        // 创建线程A执行第一个对象的静态同步方法sendEmail()
        Thread t1 = new Thread(() -> phone1.sendEmail(), "a");
        t1.start();
        
        // 主线程休眠200毫秒，确保t1先启动
        SleepUtils.sleep(200);
        
        // 创建线程B执行第二个对象的静态同步方法sendSMS()
        Thread t2 = new Thread(() -> phone2.sendSMS(), "b");
        t2.start();
    }

    /**
     * 场景5: 两个静态同步方法，一部手机
     * 结果：邮件先打印
     * 原因：静态同步方法锁定的是Class对象，方法执行顺序受调用时间影响
     */
    public static void demo5() {
        // 直接通过类名调用静态方法创建线程A
        Thread t1 = new Thread(Phone5::sendEmail, "a");
        t1.start();
        
        // 主线程休眠200毫秒，确保t1先启动
        SleepUtils.sleep(200);
        
        // 直接通过类名调用静态方法创建线程B
        Thread t2 = new Thread(Phone5::sendSMS, "b");
        t2.start();
    }

    /**
     * 场景4: 有两部手机，先打印邮件还是短信？
     * 结果：短信先打印
     * 原因：不同对象实例拥有独立的对象锁，互不影响
     */
    public static void demo4() {
        // 创建两部手机对象
        Phone3 phone1 = new Phone3();
        Phone3 phone2 = new Phone3();
        
        // 创建线程A操作第一部手机发送邮件
        Thread t1 = new Thread(phone1::sendEmail, "a");
        t1.start();
        
        // 主线程休眠200毫秒，确保t1先启动
        SleepUtils.sleep(200);
        
        // 创建线程B操作第二部手机发送短信
        Thread t2 = new Thread(phone2::sendSMS, "b");
        t2.start();
    }

    /**
     * 场景3: 添加一个普通的hello方法，先打印邮件还是hello方法？
     * 结果：hello方法先打印
     * 原因：普通方法不需要获取锁，会立即执行
     */
    public static void demo3() {
        // 创建手机对象
        Phone3 phone = new Phone3();
        
        // 创建线程A执行同步方法sendEmail()
        Thread t1 = new Thread(phone::sendEmail, "a");
        t1.start();
        
        // 主线程休眠200毫秒，确保t1先启动
        SleepUtils.sleep(200);
        
        // 创建线程B执行普通方法hello()
        Thread t2 = new Thread(phone::hello, "b");
        t2.start();
    }

    /**
     * 场景2: sendEmail方法中加入暂停秒钟，先打印邮件还是短信？
     * 结果：邮件先打印
     * 原因：同一对象的同步方法共享同一把对象锁，先获得锁的方法先执行
     */
    public static void demo2() {
        // 创建手机对象
        Phone2 phone2 = new Phone2();
        
        // 创建线程A执行sendEmail()方法
        Thread t1 = new Thread(phone2::sendEmail, "a");
        t1.start();
        
        // 主线程休眠200毫秒，确保t1先启动
        SleepUtils.sleep(200);
        
        // 创建线程B执行sendSMS()方法
        Thread t2 = new Thread(phone2::sendSMS, "b");
        t2.start();
    }

    /**
     * 场景1: 标准访问有ab两个线程，先打印邮件还是短信？
     * 结果：邮件先打印
     * 原因：同一对象的同步方法共享同一把对象锁，先启动的线程先获得锁
     */
    public static void demo1() {
        // 创建手机对象
        Phone1 phone1 = new Phone1();
        
        // 创建线程A执行sendEmail()方法
        Thread t1 = new Thread(phone1::sendEmail, "a");
        t1.start();
        
        // 主线程休眠200毫秒，确保t1先启动
        SleepUtils.sleep(200);
        
        // 创建线程B执行sendSMS()方法
        Thread t2 = new Thread(phone1::sendSMS, "b");
        t2.start();
    }
}

/**
 * Phone7类 - 演示静态同步方法与实例同步方法的区别
 */
@Slf4j
class Phone7 {
    /**
     * 静态同步方法 - 锁定的是Phone7.class对象
     * 执行时会休眠3秒后打印邮件信息
     */
    public static synchronized void sendEmail() {
        SleepUtils.sleep(3000);
        log.info("-------send Email-------");
    }

    /**
     * 实例同步方法 - 锁定的是当前实例对象(this)
     * 打印短信信息
     */
    public synchronized void sendSMS() {
        log.info("-------send SMS-------");
    }
}

/**
 * Phone5类 - 演示静态同步方法的锁机制
 */
@Slf4j
class Phone5 {
    /**
     * 静态同步方法 - 锁定的是Phone5.class对象
     * 执行时会休眠3秒后打印邮件信息
     */
    public static synchronized void sendEmail() {
        SleepUtils.sleep(3000);
        log.info("-------send Email-------");
    }

    /**
     * 静态同步方法 - 锁定的是Phone5.class对象
     * 打印短信信息
     */
    public static synchronized void sendSMS() {
        log.info("-------send SMS-------");
    }
}

/**
 * Phone3类 - 演示同步方法与普通方法的区别
 */
@Slf4j
class Phone3 {
    /**
     * 实例同步方法 - 锁定的是当前实例对象(this)
     * 执行时会休眠3秒后打印邮件信息
     */
    public synchronized void sendEmail() {
        SleepUtils.sleep(3000);
        log.info("-------send Email-------");
    }

    /**
     * 实例同步方法 - 锁定的是当前实例对象(this)
     * 打印短信信息
     */
    public synchronized void sendSMS() {
        log.info("-------send SMS-------");
    }

    /**
     * 普通方法 - 不需要获取锁，可直接执行
     * 打印问候信息
     */
    public void hello() {
        log.info("-------hello, coderlee-------");
    }
}

/**
 * Phone2类 - 演示同步方法的互斥特性
 */
@Slf4j
class Phone2 {
    /**
     * 实例同步方法 - 锁定的是当前实例对象(this)
     * 执行时会休眠3秒后打印邮件信息
     */
    public synchronized void sendEmail() {
        SleepUtils.sleep(3000);
        log.info("-------send Email-------");
    }

    /**
     * 实例同步方法 - 锁定的是当前实例对象(this)
     * 打印短信信息
     */
    public synchronized void sendSMS() {
        log.info("-------send SMS-------");
    }
}

/**
 * Phone1类 - 最基础的同步方法演示
 */
@Slf4j
class Phone1 {
    /**
     * 实例同步方法 - 锁定的是当前实例对象(this)
     * 打印邮件信息
     */
    public synchronized void sendEmail() {
        log.info("-------send Email-------");
    }

    /**
     * 实例同步方法 - 锁定的是当前实例对象(this)
     * 打印短信信息
     */
    public synchronized void sendSMS() {
        log.info("-------send SMS-------");
    }
}
