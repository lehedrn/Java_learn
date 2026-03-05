package com.coderlee.designpattern.creational.builder.classics;

/**
 * 产品类 - 电脑（复杂对象）
 * <p>
 * 这是建造者模式要创建的复杂对象，包含多个组成部分：
 * - CPU（处理器）
 * - Mainboard（主板）
 * - RAM（内存）
 * - GPU（显卡）
 * - Storage（存储器）
 * - Power（电源）
 * </p>
 * <p>
 * 特点：
 * 1. 包含多个属性，结构复杂
 * 2. 各部分属性相互独立
 * 3. 通过 setter 方法逐步设置各个部件
 * 4. 提供 toString 方法方便查看配置
 * </p>
 *
 * @author coderlee
 * @date 2026-03-05
 */
public class Computer {
    /** CPU（中央处理器）- 电脑的核心计算单元 */
    private String cpu;
    
    /** Mainboard（主板）- 连接各个硬件组件的电路板 */
    private String mainboard;
    
    /** RAM（随机存取存储器）- 临时存储运行中的数据 */
    private String ram;
    
    /** GPU（图形处理器）- 处理图形和显示输出 */
    private String gpu;
    
    /** Storage（存储器）- 长期存储数据和程序 */
    private String storage;
    
    /** Power（电源）- 为所有硬件组件供电 */
    private String power;

    /**
     * 重写 toString 方法，用于显示电脑的完整配置信息
     * <p>
     * 该方法会在日志输出和调试时被调用，返回所有硬件配置的字符串表示
     * </p>
     *
     * @return 包含所有硬件配置的格式化字符串
     */
    @Override
    public String toString() {
        return "Computer{" +
                "cpu='" + cpu + '\'' +
                ", mainboard='" + mainboard + '\'' +
                ", ram='" + ram + '\'' +
                ", gpu='" + gpu + '\'' +
                ", storage='" + storage + '\'' +
                ", power='" + power + '\'' +
                '}';
    }

    /**
     * 设置 CPU（处理器）
     *
     * @param cpu CPU 型号和规格描述
     */
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    /**
     * 设置主板
     *
     * @param mainboard 主板型号和规格描述
     */
    public void setMainboard(String mainboard) {
        this.mainboard = mainboard;
    }

    /**
     * 设置内存
     *
     * @param ram 内存容量、类型和规格描述
     */
    public void setRam(String ram) {
        this.ram = ram;
    }

    /**
     * 设置显卡
     *
     * @param gpu 显卡型号和规格描述
     */
    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    /**
     * 设置存储器
     *
     * @param storage 存储器容量、类型和规格描述
     */
    public void setStorage(String storage) {
        this.storage = storage;
    }

    /**
     * 设置电源
     *
     * @param power 电源功率、认证等级和规格描述
     */
    public void setPower(String power) {
        this.power = power;
    }
}
