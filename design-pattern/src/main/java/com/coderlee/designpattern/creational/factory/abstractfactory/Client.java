package com.coderlee.designpattern.creational.factory.abstractfactory;

import lombok.extern.slf4j.Slf4j;

/**
 * 客户端测试类
 * 
 * <p>演示抽象工厂模式的使用方式和效果。</p>
 * 
 * <p>测试流程：</p>
 * <ol>
 *     <li>创建手机套装工厂（PhoneSetFactory），获取手机和手机壳，并测试其功能</li>
 *     <li>创建笔记本套装工厂（LaptopSetFactory），获取笔记本和电脑包，并测试其功能</li>
 * </ol>
 * 
 * <p>抽象工厂模式的优点：</p>
 * <ul>
 *     <li><strong>隔离具体类的实现</strong>：客户端只需要知道抽象接口，无需关心具体产品的创建</li>
 *     <li><strong>易于交换产品系列</strong>：更换工厂即可切换整个产品族</li>
 *     <li><strong>保证产品兼容性</strong>：同一个工厂创建的产品天然可以配合使用</li>
 * </ul>
 * 
 * <p>运行结果示例：</p>
 * <pre>
 * ===============phone set===================
 * smartphone operate...
 * use phonecase
 * ============computer set===================
 * laptop operate...
 * use laptop bag
 * </pre>
 * 
 * @author coderlee
 * @version 1.0
 * @see DeviceSetFactory
 * @see PhoneSetFactory
 * @see LaptopSetFactory
 */
@Slf4j
public class Client {
    
    /**
     * 主方法：程序入口点
     * 
     * <p>通过创建不同的具体工厂实例，演示抽象工厂模式的完整使用流程。</p>
     * <p>展示了如何：</p>
     * <ul>
     *     <li>使用 PhoneSetFactory 创建手机产品族</li>
     *     <li>使用 LaptopSetFactory 创建笔记本产品族</li>
     *     <li>调用产品的方法验证功能</li>
     * </ul>
     * 
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // ========== 测试手机套装 ==========
        // 输出分隔日志，标识开始测试手机产品族
        log.info("===============phone set===================");
        
        // 创建手机套装工厂实例（实际开发中可考虑使用单例模式优化）
        DeviceSetFactory phoneSetFactory = new PhoneSetFactory();
        
        // 通过工厂创建手机并执行操作
        // 体现了抽象工厂的优势：客户端不需要知道 SmartPhone 的具体类名
        phoneSetFactory.createDevice().operate();
        
        // 通过工厂创建手机壳并使用
        // 确保配件与设备来自同一个产品族，天然兼容
        phoneSetFactory.createAccessory().use();

        // ========== 测试笔记本套装 ==========
        // 输出分隔日志，标识开始测试笔记本产品族
        log.info("============computer set===================");
        
        // 创建笔记本套装工厂实例
        DeviceSetFactory computerSetFactory = new LaptopSetFactory();
        
        // 通过工厂创建笔记本并执行操作
        // 切换产品族非常简单：只需更换工厂类
        computerSetFactory.createDevice().operate();
        
        // 通过工厂创建电脑包并使用
        // 同一个工厂创建的产品保证可以配合使用
        computerSetFactory.createAccessory().use();
    }
}
