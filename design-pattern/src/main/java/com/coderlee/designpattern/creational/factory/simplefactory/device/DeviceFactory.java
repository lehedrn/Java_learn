package com.coderlee.designpattern.creational.factory.simplefactory.device;

/**
 * 设备工厂类（简单工厂模式 - 条件判断实现）
 * <p>
 * 负责根据传入的设备类型参数创建相应的设备实例。
 * 封装了对象创建的逻辑，客户端无需关心具体产品的创建过程。
 * </p>
 * <p>
 * <b>优点：</b>
 * <ul>
 *     <li>客户端代码与具体产品类解耦</li>
 *     <li>简化了客户端的使用</li>
 * </ul>
 * <b>缺点：</b>
 * <ul>
 *     <li>违反开闭原则，新增产品类型需要修改工厂类</li>
 *     <li>条件判断过多时会影响代码可维护性</li>
 * </ul>
 * </p>
 *
 * @author coderlee
 * @date 2026-03-05
 * @see Decive
 * @see Phone
 * @see Computer
 */
public class DeviceFactory {

    /**
     * 根据设备类型创建设备实例
     * <p>
     * 通过条件判断语句，根据传入的 type 参数返回对应的设备对象。
     * 支持不区分大小写的类型匹配。
     * </p>
     *
     * @param type 设备类型标识符
     *             <ul>
     *                 <li>"phone" - 创建手机设备对象</li>
     *                 <li>"computer" - 创建电脑设备对象</li>
     *             </ul>
     * @return 返回创建的设备实例，如果类型不支持则返回 null
     * @see Decive
     * @see Phone
     * @see Computer
     */
    public static Decive createDevice(String type) {
        // 判断是否为手机类型（忽略大小写）
        if ("phone".equalsIgnoreCase(type)) {
            return new Phone();
        } 
        // 判断是否为电脑类型（忽略大小写）
        else if ("computer".equalsIgnoreCase(type)) {
            return new Computer();
        } 
        // 不支持的设备类型
        else {
            return null;
        }
    }
}
