package com.coderlee.designpattern.behavioral.template.hook;

/**
 * 具体实现：外卖订单处理器
 * <p>
 * 外卖订单需要配送，可能需要发票
 * </p>
 *
 * @author coderlee
 */
public class DeliveryOrderProcessor extends OrderProcessor {

    /**
     * 顾客地址
     */
    private final String address;

    /**
     * 是否需要发票
     */
    private final boolean needInvoice;

    public DeliveryOrderProcessor(String address, boolean needInvoice) {
        this.address = address;
        this.needInvoice = needInvoice;
    }

    @Override
    protected boolean needDelivery() {
        return true;  // 外卖需要配送
    }

    @Override
    protected void deliverToCustomer() {
        System.out.println("🛵 安排配送：骑手配送至 " + address);
    }

    @Override
    protected boolean needInvoice() {
        return needInvoice;  // 根据顾客需求
    }

    @Override
    protected void printInvoice() {
        System.out.println("📄 打印发票：电子发票将发送至顾客邮箱");
    }
}
