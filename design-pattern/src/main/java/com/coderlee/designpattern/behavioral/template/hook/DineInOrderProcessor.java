package com.coderlee.designpattern.behavioral.template.hook;

/**
 * 具体实现：堂食订单处理器
 * <p>
 * 堂食订单不需要配送，不需要发票
 * </p>
 *
 * @author coderlee
 */
public class DineInOrderProcessor extends OrderProcessor {

    @Override
    protected boolean needDelivery() {
        return false;  // 堂食不需要配送
    }

    @Override
    protected void deliverToCustomer() {
        // 堂食不执行
    }

    @Override
    protected boolean needInvoice() {
        return false;  // 默认不打印发票
    }

    @Override
    protected void printInvoice() {
        // 不执行
    }

    @Override
    protected void packFood() {
        System.out.println("🍽️  装盘：准备餐盘上菜");
    }
}
