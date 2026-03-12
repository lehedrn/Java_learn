package com.coderlee.designpattern.behavioral.template.hook;

/**
 * 具体实现：自取订单处理器
 * <p>
 * 自取订单不需要配送，可能需要发票
 * </p>
 *
 * @author coderlee
 */
public class PickupOrderProcessor extends OrderProcessor {

    /**
     * 是否需要发票
     */
    private final boolean needInvoice;

    public PickupOrderProcessor(boolean needInvoice) {
        this.needInvoice = needInvoice;
    }

    @Override
    protected boolean needDelivery() {
        return false;  // 自取不需要配送
    }

    @Override
    protected void deliverToCustomer() {
        // 自取不执行
    }

    @Override
    protected boolean needInvoice() {
        return needInvoice;
    }

    @Override
    protected void printInvoice() {
        System.out.println("📄 打印发票：纸质发票随餐品交付");
    }

    @Override
    protected void packFood() {
        System.out.println("📦 打包：装入外带包装袋");
    }

    @Override
    protected void completeOrder() {
        System.out.println("✅ 订单完成，请凭取餐码取餐");
    }
}
