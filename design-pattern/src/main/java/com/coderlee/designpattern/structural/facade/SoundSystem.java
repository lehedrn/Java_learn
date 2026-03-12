package com.coderlee.designpattern.structural.facade;

/**
 * 子系统：音响系统
 * <p>
 * 家庭影院音响系统
 * </p>
 *
 * @author coderlee
 */
public class SoundSystem {

    /**
     * 开机
     */
    public void on() {
        System.out.println("SoundSystem: 音响开机");
    }

    /**
     * 关机
     */
    public void off() {
        System.out.println("SoundSystem: 音响关机");
    }

    /**
     * 设置音量
     * @param level 音量级别
     */
    public void setVolume(int level) {
        System.out.println("SoundSystem: 设置音量为 " + level);
    }

    /**
     * 播放
     */
    public void play() {
        System.out.println("SoundSystem: 开始播放音频 🎵");
    }

    /**
     * 暂停
     */
    public void pause() {
        System.out.println("SoundSystem: 暂停播放");
    }
}
