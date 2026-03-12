package com.coderlee.designpattern.structural.adapter;

/**
 * 具体适配器：将 OldLogger 适配为 TargetLogger
 * <p>
 * 只复用 OldLogger 的 error 方法，其他方法不需要实现
 * </p>
 *
 * @author coderlee
 */
public class ErrorLoggerAdapter extends AbstractLoggerAdapter implements TargetLogger {

    @Override
    public void error(String msg) {
        // 复用旧接口的 error 方法
        System.out.println("[ERROR] " + msg);
    }

    @Override
    public void log(String msg) {
        // 将 log 方法委托给 error 方法
        error(msg);
    }
}
