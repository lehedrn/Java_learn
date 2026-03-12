package com.coderlee.designpattern.structural.facade;

/**
 * 子系统：灯光控制器
 * <p>
 * 智能灯光控制系统
 * </p>
 *
 * @author coderlee
 */
public class Lights {

    /**
     * 开灯
     */
    public void on() {
        System.out.println("Lights: 灯光打开");
    }

    /**
     * 关灯
     */
    public void off() {
        System.out.println("Lights: 灯光关闭");
    }

    /**
     * 调暗灯光
     * @param level 亮度级别 (0-100)
     */
    public void dim(int level) {
        System.out.println("Lights: 灯光调暗至 " + level + "%");
    }
}
