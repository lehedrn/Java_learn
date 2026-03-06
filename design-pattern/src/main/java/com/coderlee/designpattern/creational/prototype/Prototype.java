package com.coderlee.designpattern.creational.prototype;

/**
 * 原型模式接口
 * <p>
 * 定义了浅拷贝和深拷贝的方法规范，所有实现该接口的类都需要提供
 * 克隆自身的能力。这是原型模式的核心接口。
 * </p>
 * 
 * @author coderlee
 * @version 1.0
 * @see Monster
 */
public interface Prototype {
    /**
     * 浅拷贝方法
     * <p>
     * 创建一个新对象，但对象内部的引用类型字段仍然指向原来的对象。
     * 适用于对象内部没有可变引用类型字段，或者可以接受共享引用的场景。
     * </p>
     * 
     * @return 当前对象的浅拷贝副本
     */
    Prototype cloneShallow();
    
    /**
     * 深拷贝方法
     * <p>
     * 创建一个完全独立的新对象，包括对象内部的所有引用类型字段也会被递归复制。
     * 修改深拷贝后的对象不会影响到原始对象。
     * </p>
     * 
     * @return 当前对象的深拷贝副本
     */
    Prototype cloneDeep();
}
