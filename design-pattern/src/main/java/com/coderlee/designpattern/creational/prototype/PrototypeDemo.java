package com.coderlee.designpattern.creational.prototype;

import java.util.Random;

/**
 * 原型模式演示类
 * <p>
 * 通过实际案例展示原型模式的使用方法和效果对比。
 * 主要演示以下内容：
 * 1. 深拷贝与浅拷贝的区别
 * 2. 修改对象时对原始对象的影响
 * 3. 批量生成具有随机变异的对象
 * </p>
 *
 * @author coderlee
 * @version 1.0
 */
public class PrototypeDemo {
    /**
     * 主方法：程序入口点
     * <p>
     * 执行三个测试场景：
     * 1. 深拷贝示例：验证修改一个实例不影响其他实例
     * 2. 浅拷贝示例：演示共享引用导致的问题
     * 3. 批量生成示例：展示原型模式的高效性
     * </p>
     *
     * @param args 命令行参数（未使用）
     * @throws CloneNotSupportedException 如果克隆失败（理论上不会发生）
     */
    public static void main(String[] args) throws CloneNotSupportedException {
        // 创建随机数生成器用于变异
        Random rand = new Random();
        System.out.println("=== 使用原型模式生成怪物（深拷贝 vs 浅拷贝对比） ===\n");

        // ==================== 场景 1：深拷贝示例 ====================
        // 目标：验证深拷贝的独立性
        System.out.println("--- 深拷贝示例（修改一个不影响其他） ---");
        Monster slimeDeep1 = MonsterRegistry.create("slime", "精英", true, rand);
        Monster slimeDeep2 = MonsterRegistry.create("slime", "精英", true, rand);

        // 修改 slimeDeep2 的掉落
        slimeDeep2.addDropItem("测试物品", 999);

        System.out.println("slimeDeep1: " + slimeDeep1);
        System.out.println("slimeDeep2: " + slimeDeep2);  // 只影响自己

        // ==================== 场景 2：浅拷贝示例 ====================
        // 目标：演示浅拷贝的共享引用问题
        System.out.println("\n--- 浅拷贝示例（修改一个会影响所有） ---");
        Monster slimeShallow1 = MonsterRegistry.create("slime", "精英", false, rand);
        Monster slimeShallow2 = MonsterRegistry.create("slime", "精英", false, rand);

        // 修改 slimeShallow2 的掉落物品（会意外影响 slimeShallow1）
        slimeShallow2.addDropItem("测试物品", 999);

        System.out.println("slimeShallow1: " + slimeShallow1);  // 被意外影响！
        System.out.println("slimeShallow2: " + slimeShallow2);

        // ========= 3. 批量生成示例（使用深拷贝 + 随机变异） ============
        System.out.println("\n--- 批量生成 5 只随机变异哥布林 ---");
        for (int i = 1; i <= 5; i++) {
            Monster goblin = MonsterRegistry.create("goblin", "头目", true, rand);
            System.out.println("哥布林" + i + ": " + goblin);
        }
    }

}
