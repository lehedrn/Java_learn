package com.coderlee.designpattern.structural.facade;

/**
 * 子系统：爆米花机
 * <p>
 * 自动爆米花机
 * </p>
 *
 * @author coderlee
 */
public class PopcornPopper {

    /**
     * 开机
     */
    public void on() {
        System.out.println("PopcornPopper: 爆米花机开机");
    }

    /**
     * 关机
     */
    public void off() {
        System.out.println("PopcornPopper: 爆米花机关机");
    }

    /**
     * 放入玉米
     */
    public void putCorn() {
        System.out.println("PopcornPopper: 放入玉米粒");
    }

    /**
     * 开始爆米花
     */
    public void pop() {
        System.out.println("PopcornPopper: 开始爆米花 🍿");
    }
}
