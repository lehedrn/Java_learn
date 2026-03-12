package com.coderlee.designpattern.structural.adapter;

/**
 * 抽象适配器：为 OldLogger 提供空实现
 * <p>
 * 这种模式也叫"缺省适配器模式"
 * 当只需要使用接口中的部分方法时非常有用
 * </p>
 *
 * @author coderlee
 */
public abstract class AbstractLoggerAdapter implements OldLogger {
    @Override
    public void debug(String msg) {
        // 默认空实现，子类按需重写
    }

    @Override
    public void info(String msg) {
        // 默认空实现，子类按需重写
    }

    @Override
    public void warn(String msg) {
        // 默认空实现，子类按需重写
    }

    @Override
    public void error(String msg) {
        // 默认空实现，子类按需重写
    }
}
