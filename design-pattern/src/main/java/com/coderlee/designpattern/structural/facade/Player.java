package com.coderlee.designpattern.structural.facade;

/**
 * 子系统：播放器
 * <p>
 * DVD/蓝光播放器
 * </p>
 *
 * @author coderlee
 */
public class Player {

    /**
     * 开机
     */
    public void on() {
        System.out.println("Player: 播放器开机");
    }

    /**
     * 关机
     */
    public void off() {
        System.out.println("Player: 播放器关机");
    }

    /**
     * 播放
     */
    public void play(String movie) {
        System.out.println("Player: 播放电影《" + movie + "》 🎬");
    }

    /**
     * 暂停
     */
    public void pause() {
        System.out.println("Player: 暂停播放");
    }

    /**
     * 停止
     */
    public void stop() {
        System.out.println("Player: 停止播放");
    }
}
