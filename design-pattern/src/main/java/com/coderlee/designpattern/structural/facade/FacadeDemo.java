package com.coderlee.designpattern.structural.facade;

/**
 * 外观模式演示类
 * <p>
 * 通过家庭影院系统展示外观模式的使用
 * </p>
 *
 * @author coderlee
 */
public class FacadeDemo {

    public static void main(String[] args) {
        System.out.println("========== 外观模式演示 - 家庭影院系统 ==========\n");

        // 创建所有子系统
        Projector projector = new Projector();
        PopcornPopper popcornPopper = new PopcornPopper();
        SoundSystem soundSystem = new SoundSystem();
        Player player = new Player();
        Lights lights = new Lights();

        // 创建外观类
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(
                projector, popcornPopper, soundSystem, player, lights
        );

        // 一键观影（无需关心子系统的复杂操作）
        homeTheater.watchMovie("流浪地球");

        // 暂停
        homeTheater.pauseMovie();

        // 继续
        homeTheater.resumeMovie();

        // 结束观影
        homeTheater.endMovie();

        System.out.println("\n========== 演示结束 ==========");
    }
}
