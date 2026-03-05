package com.coderlee.designpattern.creational.builder.classics;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体建造者 - 游戏电脑建造者
 * <p>
 * 实现 ComputerBuilder 接口，专门用于构建高性能游戏电脑。
 * 每个构建方法都会设置适合游戏场景的高端硬件配置：
 * - 高性能 CPU（Intel Core i9）
 * - 高端游戏主板（ASUS ROG）
 * - 大容量高频内存（32GB DDR5）
 * - 顶级游戏显卡（RTX 4090）
 * - 高速固态硬盘（2TB NVMe）
 * - 大功率高品质电源（850W 80+ Gold）
 * </p>
 * <p>
 * 特点：
 * 1. 专注于游戏性能，不考虑成本
 * 2. 所有配件都选择高端产品
 * 3. 注重散热和超频能力
 * 4. 带有 RGB 灯效等游戏元素
 * </p>
 *
 * @author coderlee
 * @date 2026-03-05
 * @see ComputerBuilder 抽象建造者接口
 * @see OfficeComputerBuilder 对比：办公电脑建造者
 */
@Slf4j
public class GamingComputerBuilder implements ComputerBuilder {
    /** 正在构建的游戏电脑实例 */
    private Computer computer = new Computer();

    /**
     * 构建游戏电脑的 CPU
     * <p>
     * 选择 Intel Core i9-13900K，这是高性能游戏处理器，
     * 具有多核心高频率特性，适合游戏和高负载任务
     * </p>
     */
    @Override
    public void buildCpu() {
        // 设置高性能游戏 CPU
        computer.setCpu("Intel Core i9-13900K (高性能游戏 CPU)");
    }

    /**
     * 构建游戏电脑的主板
     * <p>
     * 选择 ASUS ROG Z790 高端游戏主板，
     * 支持超频、多显卡、高速内存等游戏特性
     * </p>
     */
    @Override
    public void buildMainBoard() {
        // 设置高端游戏主板
        computer.setMainboard("ASUS ROG Z790 (高端游戏主板)");
    }

    /**
     * 构建游戏电脑的内存
     * <p>
     * 选择 32GB 大容量 DDR5 6000MHz 高频内存，
     * 带 RGB 灯效，满足游戏和多任务需求
     * </p>
     */
    @Override
    public void buildRam() {
        // 设置大容量高频游戏内存
        computer.setRam("32GB DDR5 6000MHz (RGB 灯效)");
    }

    /**
     * 构建游戏电脑的显卡
     * <p>
     * 选择 NVIDIA RTX 4090 顶级游戏显卡，
     * 支持 4K 高画质游戏和光线追踪技术
     * </p>
     */
    @Override
    public void buildGpu() {
        // 设置顶级游戏显卡
        computer.setGpu("NVIDIA RTX 4090 (顶级游戏显卡)");
    }

    /**
     * 构建游戏电脑的存储器
     * <p>
     * 选择 2TB NVMe 高速固态硬盘，
     * 提供快速的游戏加载速度和充足的存储空间
     * </p>
     */
    @Override
    public void buildStorage() {
        // 设置高速大容量游戏存储
        computer.setStorage("2TB NVMe SSD (高速游戏存储)");
    }

    /**
     * 构建游戏电脑的电源
     * <p>
     * 选择 850W 80+ Gold 认证全模组电源，
     * 为高功耗硬件提供稳定充足的电力供应
     * </p>
     */
    @Override
    public void buildPower() {
        // 设置大功率高品质电源
        computer.setPower("850W 80+ Gold (全模组电源)");
    }

    /**
     * 获取构建完成的游戏电脑
     * <p>
     * 在所有部件构建完成后，返回完整的游戏电脑对象
     * </p>
     *
     * @return 配置完整的高性能游戏电脑
     */
    @Override
    public Computer getResult() {
        return computer;
    }
}
