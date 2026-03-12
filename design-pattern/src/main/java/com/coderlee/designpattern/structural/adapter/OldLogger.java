package com.coderlee.designpattern.structural.adapter;

/**
 * 被适配者：旧的日志接口
 * <p>
 * 系统中已存在的旧接口，有多个方法
 * 但客户端只需要使用其中一部分方法
 * </p>
 *
 * @author coderlee
 */
public interface OldLogger {
    void debug(String msg);
    void info(String msg);
    void warn(String msg);
    void error(String msg);
}
