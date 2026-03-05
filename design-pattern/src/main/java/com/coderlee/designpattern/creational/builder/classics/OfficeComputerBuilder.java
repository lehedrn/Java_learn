package com.coderlee.designpattern.creational.builder.classics;

/**
 * 具体建造者 - 办公电脑建造者
 * <p>
 * 实现 ComputerBuilder 接口，专门用于构建高效办公电脑。
 * 每个构建方法都会设置适合办公场景的实用硬件配置：
 * - 中端 CPU（Intel Core i5）
 * - 稳定办公主板（Gigabyte B660）
 * - 标准容量内存（16GB DDR4）
 * - 集成显卡（无需独立显卡）
 * - 基本固态硬盘（512GB SSD）
 * - 经济型电源（500W 80+ Bronze）
 * </p>
 * <p>
 * 特点：
 * 1. 注重性价比，满足办公需求即可
 * 2. 所有配件选择实用型产品
 * 3. 低功耗，节能环保
 * 4. 稳定性优先，适合长时间运行
 * </p>
 *
 * @author coderlee
 * @date 2026-03-05
 * @see ComputerBuilder 抽象建造者接口
 * @see GamingComputerBuilder 对比：游戏电脑建造者
 */
public class OfficeComputerBuilder implements ComputerBuilder {
    /** 正在构建的办公电脑实例 */
    private Computer computer = new Computer();

    /**
     * 构建办公电脑的 CPU
     * <p>
     * 选择 Intel Core i5-12400，这是高效办公处理器，
     * 性能足够处理文档、网页、视频会议等办公任务，功耗低
     * </p>
     */
    @Override
    public void buildCpu() {
        // 设置高效办公 CPU
        computer.setCpu("Intel Core i5-12400 (高效办公 CPU)");
    }

    /**
     * 构建办公电脑的主板
     * <p>
     * 选择 Gigabyte B660 稳定办公主板，
     * 性价比高，稳定性好，满足日常办公需求
     * </p>
     */
    @Override
    public void buildMainBoard() {
        // 设置稳定办公主板
        computer.setMainboard("Gigabyte B660 (稳定办公主板)");
    }

    /**
     * 构建办公电脑的内存
     * <p>
     * 选择 16GB DDR4 3200MHz 标准内存，
     * 容量足够多任务办公使用，成本合理
     * </p>
     */
    @Override
    public void buildRam() {
        // 设置标准办公内存
        computer.setRam("16GB DDR4 3200MHz (标准办公内存)");
    }

    /**
     * 构建办公电脑的显卡
     * <p>
     * 使用 CPU 集成显卡，办公场景无需独立显卡，
     * 节省成本和功耗，足以驱动办公显示器
     * </p>
     */
    @Override
    public void buildGpu() {
        // 使用集成显卡，节约成本
        computer.setGpu("集成显卡 (无需独立显卡)");
    }

    /**
     * 构建办公电脑的存储器
     * <p>
     * 选择 512GB SSD 固态硬盘，
     * 启动速度快，满足办公文件和软件安装需求
     * </p>
     */
    @Override
    public void buildStorage() {
        // 设置基本办公存储
        computer.setStorage("512GB SSD (基本办公存储)");
    }

    /**
     * 构建办公电脑的电源
     * <p>
     * 选择 500W 80+ Bronze 经济型电源，
     * 功率足够且节能，符合办公电脑低功耗特性
     * </p>
     */
    @Override
    public void buildPower() {
        // 设置经济型电源
        computer.setPower("500W 80+ Bronze (经济型电源)");
    }

    /**
     * 获取构建完成的办公电脑
     * <p>
     * 在所有部件构建完成后，返回完整的办公电脑对象
     * </p>
     *
     * @return 配置完整的实用型办公电脑
     */
    @Override
    public Computer getResult() {
        return computer;
    }
}
