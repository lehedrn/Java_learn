package com.coderlee.designpattern.structural.facade;

/**
 * 子系统：投影仪
 * <p>
 * 家庭影院的投影仪设备
 * </p>
 *
 * @author coderlee
 */
public class Projector {

    /**
     * 开机
     */
    public void on() {
        System.out.println("Projector: 投影仪开机");
    }

    /**
     * 关机
     */
    public void off() {
        System.out.println("Projector: 投影仪关机");
    }

    /**
     * 设置输入源
     * @param source 输入源
     */
    public void setInput(String source) {
        System.out.println("Projector: 设置输入源为 " + source);
    }
}
