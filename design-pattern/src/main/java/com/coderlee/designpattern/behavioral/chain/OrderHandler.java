package com.coderlee.designpattern.behavioral.chain;

/**
 * 抽象处理者 (Abstract Handler)
 * 定义处理者的公共接口
 *
 * 设计说明：
 * 本实现采用"流水线"模式，每个处理器都会执行，全部通过才算成功
 * 适用于：订单处理、审批流程、数据过滤等场景
 */
public abstract class OrderHandler {

    /**
     * 下一个处理者
     */
    protected OrderHandler nextHandler;

    /**
     * 设置下一个处理者
     * @return 返回下一个处理者，支持链式调用
     */
    public OrderHandler setNext(OrderHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    /**
     * 处理请求的模板方法
     * @param order 订单
     * @return true 表示整条链处理成功，false 表示失败
     */
    public final boolean handle(Order order) {
        log("开始处理订单：" + order.getOrderId());

        // 执行当前处理器的逻辑
        boolean currentResult = doHandle(order);

        // 如果当前处理器失败，直接返回 false
        if (!currentResult) {
            log("当前处理器执行失败，终止链条");
            return false;
        }

        // 如果存在下一个处理者，继续传递
        if (nextHandler != null) {
            log("传递给下一个处理者 >>>");
            return nextHandler.handle(order);
        }

        // 到达链尾，全部处理完成
        log("订单处理完成 ✓");
        return true;
    }

    /**
     * 具体处理逻辑，由子类实现
     * @param order 订单
     * @return true 表示处理通过，false 表示处理失败需终止流程
     */
    protected abstract boolean doHandle(Order order);

    /**
     * 日志方法
     */
    protected void log(String message) {
        System.out.println("[" + getClass().getSimpleName() + "] " + message);
    }
}
