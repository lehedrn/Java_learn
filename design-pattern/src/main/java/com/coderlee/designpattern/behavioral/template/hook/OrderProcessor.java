package com.coderlee.designpattern.behavioral.template.hook;

/**
 * 抽象类：外卖订单处理模板
 * <p>
 * 定义订单处理的标准流程，包含钩子方法供子类扩展
 * </p>
 *
 * @author coderlee
 */
public abstract class OrderProcessor {

    /**
     * 模板方法：订单处理流程
     */
    public final void processOrder() {
        System.out.println("\n=== 开始处理订单 ===");
        receiveOrder();           // 接收订单
        prepareFood();            // 备餐
        packFood();               // 打包
        arrangeDelivery();        // 配送安排（钩子）
        printReceipt();           // 打印小票（钩子）
        completeOrder();          // 完成订单
        System.out.println("=== 订单处理完成 ===\n");
    }

    /**
     * 具体方法：接收订单
     */
    protected void receiveOrder() {
        System.out.println("📱 接收订单：确认订单信息");
    }

    /**
     * 具体方法：备餐
     */
    protected void prepareFood() {
        System.out.println("👨‍🍳 备餐：制作餐品");
    }

    /**
     * 具体方法：打包
     */
    protected void packFood() {
        System.out.println("📦 打包：装入包装袋");
    }

    /**
     * 钩子方法：安排配送
     * <p>
     * 子类可以选择性重写，默认不执行
     * </p>
     * @return 是否需要配送
     */
    protected boolean needDelivery() {
        return false;
    }

    /**
     * 钩子方法：安排配送的具体实现
     */
    protected void arrangeDelivery() {
        if (needDelivery()) {
            deliverToCustomer();
        } else {
            System.out.println("🚶 顾客自取/堂食");
        }
    }

    /**
     * 具体方法：配送给顾客
     */
    protected abstract void deliverToCustomer();

    /**
     * 钩子方法：是否需要打印发票
     * @return 是否需要发票
     */
    protected boolean needInvoice() {
        return false;
    }

    /**
     * 具体方法：打印小票（包含钩子判断）
     */
    protected void printReceipt() {
        System.out.println("🧾 打印订单小票");
        if (needInvoice()) {
            printInvoice();
        }
    }

    /**
     * 抽象方法：打印发票
     */
    protected abstract void printInvoice();

    /**
     * 具体方法：完成订单
     */
    protected void completeOrder() {
        System.out.println("✅ 订单完成，感谢惠顾");
    }
}
