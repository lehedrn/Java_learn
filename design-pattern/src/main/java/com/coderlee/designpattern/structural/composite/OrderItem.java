package com.coderlee.designpattern.structural.composite;

/**
 * 组件接口：订单项
 * <p>
 * 定义了商品和商品组合的公共行为
 * </p>
 *
 * @author coderlee
 */
public interface OrderItem {
    /**
     * 显示商品信息
     * @param indent 缩进
     */
    void show(String indent);

    /**
     * 获取价格
     * @return 价格
     */
    double getPrice();

    /**
     * 获取数量
     * @return 数量
     */
    int getQuantity();

    /**
     * 添加商品（叶子节点不支持）
     * @param item 商品
     */
    default void add(OrderItem item) {
        throw new UnsupportedOperationException("单个商品不支持添加操作");
    }

    /**
     * 移除商品（叶子节点不支持）
     * @param item 商品
     */
    default void remove(OrderItem item) {
        throw new UnsupportedOperationException("单个商品不支持移除操作");
    }
}
