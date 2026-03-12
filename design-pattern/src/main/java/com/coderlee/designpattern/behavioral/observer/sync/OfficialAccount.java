package com.coderlee.designpattern.behavioral.observer.sync;

import java.util.ArrayList;
import java.util.List;

/**
 * 被观察者：微信公众号
 * <p>
 * 管理粉丝列表，发布文章时通知所有粉丝
 * </p>
 *
 * @author coderlee
 */
public class OfficialAccount {

    /**
     * 公众号名称
     */
    private final String name;

    /**
     * 粉丝列表（观察者列表）
     */
    private final List<Observer> fans;

    public OfficialAccount(String name) {
        this.name = name;
        this.fans = new ArrayList<>();
    }

    /**
     * 添加粉丝（订阅）
     *
     * @param fan 粉丝
     */
    public void attach(Observer fan) {
        fans.add(fan);
        System.out.println("✅ " + fan.getName() + " 关注了公众号 [" + name + "]");
    }

    /**
     * 移除粉丝（取消订阅）
     *
     * @param fan 粉丝
     */
    public void detach(Observer fan) {
        fans.remove(fan);
        System.out.println("❌ " + fan.getName() + " 取消关注公众号 [" + name + "]");
    }

    /**
     * 发布文章，通知所有粉丝
     * <p>
     * 同步阻塞方式：逐个通知每个粉丝，等待处理完成
     * </p>
     *
     * @param article 文章标题
     */
    public void publishArticle(String article) {
        System.out.println("\n📢 公众号 [" + name + "] 发布新文章：《" + article + "》");
        long startTime = System.currentTimeMillis();

        // 同步阻塞通知所有粉丝
        for (Observer fan : fans) {
            fan.update("《" + article + "》");
        }

        long endTime = System.currentTimeMillis();
        System.out.println("⏱️ 同步通知完成，耗时：" + (endTime - startTime) + "ms\n");
    }

    /**
     * 获取粉丝数量
     *
     * @return 粉丝数量
     */
    public int getFanCount() {
        return fans.size();
    }
}
