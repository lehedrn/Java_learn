package com.coderlee.designpattern.behavioral.observer.sync;

/**
 * 具体观察者：粉丝
 * <p>
 * 实现观察者接口，接收公众号的文章推送
 * </p>
 *
 * @author coderlee
 */
public class Fan implements Observer {

    /**
     * 粉丝名称
     */
    private final String name;

    public Fan(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        // 同步接收消息，会阻塞主线程
        System.out.println("📱 粉丝 [" + name + "] 收到推送：" + message);
        // 模拟阅读文章需要时间
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
