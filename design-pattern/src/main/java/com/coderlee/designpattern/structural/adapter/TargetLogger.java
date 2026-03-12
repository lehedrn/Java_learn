package com.coderlee.designpattern.structural.adapter;

/**
 * 目标接口：简化的日志接口
 * <p>
 * 客户端只需要一个简单的日志方法
 * </p>
 *
 * @author coderlee
 */
public interface TargetLogger {
    void log(String msg);
}
