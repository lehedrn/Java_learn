package com.coderlee.designpattern.creational.builder.simple;

/**
 * 产品类 - 电脑（复杂对象）
 * <p>
 * 这是一个不可变类（Immutable Class），所有属性都是 final 的：
 * 1. 一旦构建完成，属性不能被修改
 * 2. 保证了对象的线程安全性和状态一致性
 * 3. 通过 Builder 模式优雅地创建不可变对象
 * </p>
 * <p>
 * 设计要点：
 * - 私有构造函数，只能通过 Builder 创建实例
 * - 所有字段都是 final，确保不可变性
 * - 包含必选字段和可选字段（由 Builder 管理默认值）
 * </p>
 *
 * @author coderlee
 * @date 2026-03-05
 */
public class Computer {
    /** CPU（中央处理器）- 必选参数 */
    private final String cpu;
    
    /** Mainboard（主板）- 必选参数 */
    private final String mainboard;
    
    /** RAM（内存）- 可选参数，有默认值 */
    private final String ram;
    
    /** GPU（显卡）- 可选参数，有默认值 */
    private final String gpu;
    
    /** Storage（存储器）- 可选参数，有默认值 */
    private final String storage;
    
    /** Power（电源）- 可选参数，有默认值 */
    private final String power;

    /**
     * 私有构造函数，防止外部直接实例化
     * <p>
     * 只能通过内部 Builder 类的 build() 方法调用此构造函数，
     * 确保所有 Computer 对象都是通过 Builder 正确构建的。
     * </p>
     *
     * @param builder 已经配置好的 Builder 实例，包含所有必要的参数
     */
    private Computer(Builder builder) {
        // 从 Builder 中复制所有属性值到当前对象
        this.cpu = builder.cpu;
        this.mainboard = builder.mainboard;
        this.ram = builder.ram;
        this.gpu = builder.gpu;
        this.storage = builder.storage;
        this.power = builder.power;
    }

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
     * 静态内部建造者类 - 负责构建 Computer 对象
     * <p>
     * Builder 模式的核心实现：
     * 1. 将必选参数作为构造器参数，确保必填
     * 2. 可选参数提供默认值，减少配置负担
     * 3. 每个 setter 方法返回 this，支持链式调用
     * 4. build() 方法创建最终的 Computer 对象
     * </p>
     * <p>
     * 链式调用示例：
     * new Builder(cpu, mainboard).ram("32GB").gpu("RTX4090").build()
     * </p>
     *
     * @author coderlee
     * @date 2026-03-05
     */
    public static class Builder {
        // ========== 必选参数（必须通过构造函数传入）==========
        
        /** CPU（中央处理器）- 无默认值，必须指定 */
        private final String cpu;
        
        /** Mainboard（主板）- 无默认值，必须指定 */
        private final String mainboard;
        
        // ========== 可选参数（有合理的默认值）==========
        
        /** 
         * RAM（内存）- 可选参数
         * 默认值："16GB DDR4" - 满足基本办公需求
         */
        private String ram = "16GB DDR4";
        
        /** 
         * GPU（显卡）- 可选参数
         * 默认值："集成显卡" - 办公场景无需独立显卡
         */
        private String gpu = "集成显卡";
        
        /** 
         * Storage（存储器）- 可选参数
         * 默认值："512GB SSD" - 基本办公存储容量
         */
        private String storage = "512GB SSD";
        
        /** 
         * Power（电源）- 可选参数
         * 默认值："500W" - 标准办公电脑功率
         */
        private String power = "500W";

        /**
         * Builder 构造函数 - 初始化必选参数
         * <p>
         * 强制调用者必须提供 CPU 和主板参数，
         * 这是构建一台电脑最基本的要求。
         * </p>
         *
         * @param cpu CPU 型号和规格（必选）
         * @param mainboard 主板型号和规格（必选）
         */
        public Builder(String cpu, String mainboard) {
            this.cpu = cpu;
            this.mainboard = mainboard;
        }

        /**
         * 设置内存（RAM）
         * <p>
         * 链式调用方法，可以连续调用多个 setter
         * </p>
         *
         * @param ram 内存容量、类型和规格描述
         * @return 返回当前 Builder 实例，支持链式调用
         */
        public Builder ram(String ram) {
            this.ram = ram;
            return this;  // 返回 this，实现链式调用
        }

        /**
         * 设置显卡（GPU）
         * <p>
         * 链式调用方法，可以连续调用多个 setter
         * </p>
         *
         * @param gpu 显卡型号和规格描述
         * @return 返回当前 Builder 实例，支持链式调用
         */
        public Builder gpu(String gpu) {
            this.gpu = gpu;
            return this;  // 返回 this，实现链式调用
        }

        /**
         * 设置存储器（Storage）
         * <p>
         * 链式调用方法，可以连续调用多个 setter
         * </p>
         *
         * @param storage 存储器容量、类型和规格描述
         * @return 返回当前 Builder 实例，支持链式调用
         */
        public Builder storage(String storage) {
            this.storage = storage;
            return this;  // 返回 this，实现链式调用
        }

        /**
         * 设置电源（Power）
         * <p>
         * 链式调用方法，可以连续调用多个 setter
         * </p>
         *
         * @param power 电源功率、认证等级和规格描述
         * @return 返回当前 Builder 实例，支持链式调用
         */
        public Builder power(String power) {
            this.power = power;
            return this;  // 返回 this，实现链式调用
        }

        /**
         * 构建并返回最终的 Computer 对象
         * <p>
         * 在所有参数设置完成后调用此方法，
         * 会调用私有的 Computer 构造函数创建不可变对象。
         * </p>
         *
         * @return 构建完成的 Computer 对象
         */
        public Computer build() {
            return new Computer(this);
        }
    }
}
