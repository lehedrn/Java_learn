package com.coderlee.designpattern.creational.builder.classics;

/**
 * 指挥者类 - 控制建造流程的执行顺序
 * <p>
 * Director 的职责：
 * 1. 持有抽象建造者的引用
 * 2. 按照固定顺序调用建造者的各个构建方法
 * 3. 确保产品的完整性（所有部件都被正确构建）
 * 4. 提供获取最终产品的接口
 * </p>
 * <p>
 * 设计优势：
 * - 将构建流程与具体建造者解耦
 * - 同一套流程可以适配不同的建造者
 * - 便于控制构建顺序和质量检查
 * </p>
 *
 * @author coderlee
 * @date 2026-03-05
 * @see ComputerBuilder 抽象建造者接口
 */
public class ComputerDirector {
    /** 持有的建造者引用，用于控制构建过程 */
    private ComputerBuilder builder;
    
    /**
     * 设置建造者
     * <p>
     * 将具体建造者注入到指挥者中，以便后续控制构建流程。
     * 可以在运行时动态更换建造者，从而构建不同类型的产品。
     * </p>
     *
     * @param builder 实现了 ComputerBuilder 接口的具体建造者对象
     */
    public void setBuilder(ComputerBuilder builder) {
        this.builder = builder;
    }
    
    /**
     * 构建电脑的完整流程
     * <p>
     * 该方法按照固定的顺序调用建造者的各个构建方法：
     * 1. 构建 CPU
     * 2. 构建主板
     * 3. 构建内存
     * 4. 构建显卡
     * 5. 构建存储器
     * 6. 构建电源
     * </p>
     * <p>
     * 在执行前会检查 builder 是否为 null，确保安全性
     * </p>
     *
     * @throws IllegalStateException 如果 builder 未设置（为 null）则抛出此异常
     */
    public void constructComputer() {
        // 检查 builder 是否已设置，未设置则抛出异常
        if (null == builder) {
            throw new IllegalStateException("builder is null");
        }
        
        // 按照固定顺序依次构建各个硬件组件
        // 这个顺序是组装电脑的标准流程
        builder.buildCpu();         // 第一步：安装 CPU
        builder.buildMainBoard();   // 第二步：安装主板
        builder.buildRam();         // 第三步：安装内存
        builder.buildGpu();         // 第四步：安装显卡
        builder.buildStorage();     // 第五步：安装存储器
        builder.buildPower();       // 第六步：安装电源
    }
    
    /**
     * 获取构建完成的电脑产品
     * <p>
     * 在 constructComputer() 执行完成后调用，
     * 返回已经组装好的完整电脑对象。
     * </p>
     *
     * @return 构建完成的 Computer 对象
     * @throws NullPointerException 如果 builder 为 null 可能抛出此异常
     */
    public Computer getComputer() {
        // 委托给当前建造者获取最终产品
        return builder.getResult();
    }
}
