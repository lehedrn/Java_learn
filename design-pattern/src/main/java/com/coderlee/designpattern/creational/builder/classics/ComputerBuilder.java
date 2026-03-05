package com.coderlee.designpattern.creational.builder.classics;

/**
 * 抽象建造者接口 - 定义电脑构建的标准
 * <p>
 * 该接口定义了构建一个完整电脑所需的所有步骤：
 * - buildCpu(): 构建 CPU
 * - buildMainBoard(): 构建主板
 * - buildRam(): 构建内存
 * - buildGpu(): 构建显卡
 * - buildStorage(): 构建存储器
 * - buildPower(): 构建电源
 * - getResult(): 获取最终产品
 * </p>
 * <p>
 * 设计要点：
 * 1. 定义了构建流程的标准步骤
 * 2. 不关心具体实现细节
 * 3. 由具体建造者实现各个步骤
 * 4. 保证所有建造者都能生产完整的电脑
 * </p>
 *
 * @author coderlee
 * @date 2026-03-05
 * @see GamingComputerBuilder 游戏电脑建造者实现
 * @see OfficeComputerBuilder 办公电脑建造者实现
 */
public interface ComputerBuilder {
    /**
     * 构建 CPU（中央处理器）
     * <p>
     * 实现类需要在此方法中设置合适的 CPU 型号
     * </p>
     */
    void buildCpu();
    
    /**
     * 构建主板（Mainboard）
     * <p>
     * 实现类需要在此方法中设置兼容的主板型号
     * </p>
     */
    void buildMainBoard();
    
    /**
     * 构建内存（RAM）
     * <p>
     * 实现类需要在此方法中设置合适的内存容量和规格
     * </p>
     */
    void buildRam();
    
    /**
     * 构建显卡（GPU）
     * <p>
     * 实现类需要在此方法中设置合适的显卡型号
     * </p>
     */
    void buildGpu();
    
    /**
     * 构建存储器（Storage）
     * <p>
     * 实现类需要在此方法中设置合适的存储设备
     * </p>
     */
    void buildStorage();
    
    /**
     * 构建电源（Power Supply）
     * <p>
     * 实现类需要在此方法中设置合适功率的电源
     * </p>
     */
    void buildPower();

    /**
     * 获取构建完成的电脑产品
     * <p>
     * 当所有构建步骤完成后，调用此方法获取最终的 Computer 对象
     * </p>
     *
     * @return 构建完成的 Computer 对象
     */
    Computer getResult();
}
