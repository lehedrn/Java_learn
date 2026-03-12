package com.coderlee.designpattern.structural.facade;

/**
 * 外观类：家庭影院外观
 * <p>
 * 为家庭影院系统提供统一的简化接口
 * 客户端只需要调用外观类的方法，无需关心子系统的细节
 * </p>
 *
 * @author coderlee
 */
public class HomeTheaterFacade {

    private Projector projector;
    private PopcornPopper popcornPopper;
    private SoundSystem soundSystem;
    private Player player;
    private Lights lights;

    public HomeTheaterFacade(Projector projector, PopcornPopper popcornPopper,
                             SoundSystem soundSystem, Player player, Lights lights) {
        this.projector = projector;
        this.popcornPopper = popcornPopper;
        this.soundSystem = soundSystem;
        this.player = player;
        this.lights = lights;
    }

    /**
     * 一键观影模式
     * <p>
     * 自动完成所有观影前的准备工作
     * </p>
     */
    public void watchMovie(String movie) {
        System.out.println("========== 准备观影 ==========");

        // 准备爆米花
        popcornPopper.on();
        popcornPopper.putCorn();
        popcornPopper.pop();

        // 调暗灯光
        lights.dim(30);

        // 打开投影仪
        projector.on();
        projector.setInput("HDMI-1");

        // 打开音响
        soundSystem.on();
        soundSystem.setVolume(50);

        // 播放电影
        player.on();
        player.play(movie);

        System.out.println("========== 观影准备就绪，开始享受电影 ==========\n");
    }

    /**
     * 一键结束观影
     * <p>
     * 自动关闭所有设备
     * </p>
     */
    public void endMovie() {
        System.out.println("========== 结束观影 ==========");

        // 停止播放
        player.stop();
        player.off();

        // 关闭音响
        soundSystem.off();

        // 关闭投影仪
        projector.off();

        // 打开灯光
        lights.on();

        // 关闭爆米花机
        popcornPopper.off();

        System.out.println("========== 已关闭所有设备，感谢观看 ==========\n");
    }

    /**
     * 暂停观影
     */
    public void pauseMovie() {
        System.out.println("--- 暂停观影 ---");
        player.pause();
        soundSystem.pause();
        lights.dim(50);
    }

    /**
     * 继续观影
     */
    public void resumeMovie() {
        System.out.println("--- 继续观影 ---");
        lights.dim(30);
        player.play("");
        soundSystem.play();
    }
}
