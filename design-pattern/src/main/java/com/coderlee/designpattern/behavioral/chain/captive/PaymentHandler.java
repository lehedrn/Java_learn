package com.coderlee.designpattern.behavioral.chain.captive;

/**
 * 抽象处理者 - 捕获式实现
 *
 * 核心逻辑：
 * - 如果当前处理者能处理，则处理并返回 true，链条终止
 * - 如果当前处理者不能处理，则传递给下一个处理者
 *
 * 适用场景：事件处理、命令处理、URL 路由等
 */
public abstract class PaymentHandler {

    /**
     * 下一个处理者
     */
    protected PaymentHandler nextHandler;

    /**
     * 设置下一个处理者
     */
    public PaymentHandler setNext(PaymentHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    /**
     * 处理请求（模板方法，final 防止子类修改流程）
     *
     * @param paymentContext 支付上下文
     * @return true 表示已处理，false 表示无合适处理者
     */
    public final boolean handle(PaymentContext paymentContext) {
        log("尝试处理支付：" + paymentContext);

        // 判断当前处理者是否能处理
        if (canHandle(paymentContext)) {
            log("当前处理者可以处理");
            boolean result = doHandle(paymentContext);
            if (result) {
                log("✓ 支付处理完成，链条终止");
            }
            return result;
        }

        // 当前处理者不能处理，传递给下一个
        if (nextHandler != null) {
            log("当前处理者无法处理，转交下一个 >>>");
            return nextHandler.handle(paymentContext);
        }

        // 到达链尾，没有处理者
        log("✗ 无合适的支付渠道");
        paymentContext.setResult("失败：无可用支付渠道");
        return false;
    }

    /**
     * 判断当前处理者是否能处理该请求
     * 由子类实现判断逻辑
     */
    protected abstract boolean canHandle(PaymentContext paymentContext);

    /**
     * 具体处理逻辑
     * 由子类实现
     */
    protected abstract boolean doHandle(PaymentContext paymentContext);

    /**
     * 日志
     */
    protected void log(String message) {
        System.out.println("[" + getClass().getSimpleName() + "] " + message);
    }
}
