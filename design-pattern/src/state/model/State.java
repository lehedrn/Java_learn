package design_pattern.src.state.model;

import design_pattern.src.state.context.OrderContext;

/**
 * 订单状态接口
 * 定义所有具体状态类需要实现的行为
 */
public interface State {

    /**
     * 支付操作
     * @param context 订单上下文
     */
    void pay(OrderContext context);

    /**
     * 发货操作
     * @param context 订单上下文
     */
    void ship(OrderContext context);

    /**
     * 收货操作
     * @param context 订单上下文
     */
    void receive(OrderContext context);

    /**
     * 取消订单操作
     * @param context 订单上下文
     */
    void cancel(OrderContext context);

    /**
     * 获取当前状态名称
     * @return 状态名称
     */
    String getName();
}
