package com.coderlee.designpattern.behavioral.observer.sync;

/**
 * 同步阻塞观察者模式演示 - 微信公众号通知
 * <p>
 * 演示场景：
 * 1. 公众号发布文章
 * 2. 同步通知所有粉丝（逐个通知，阻塞等待）
 * 3. 统计通知耗时
 * </p>
 *
 * @author coderlee
 */
public class SyncDemo {

    public static void main(String[] args) {
        System.out.println("========== 观察者模式 - 同步阻塞演示 ==========");
        System.out.println("========== 微信公众号通知 ==========\n");

        // 创建公众号
        OfficialAccount account = new OfficialAccount("Coder 技术堂");

        // 创建粉丝（观察者）
        Observer fan1 = new Fan("张三");
        Observer fan2 = new Fan("李四");
        Observer fan3 = new Fan("王五");
        Observer fan4 = new Fan("赵六");

        // 订阅公众号
        account.attach(fan1);
        account.attach(fan2);
        account.attach(fan3);
        account.attach(fan4);

        System.out.println("\n当前粉丝数量：" + account.getFanCount());

        // 发布第一篇文章
        account.publishArticle("深入理解观察者模式");

        // 取消订阅
        account.detach(fan2);

        // 发布第二篇文章
        account.publishArticle("Java 并发编程实战");

        System.out.println("========== 演示结束 ==========");
    }
}
