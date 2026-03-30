package design_pattern.src.state.state;

import design_pattern.src.state.context.OrderContext;
import design_pattern.src.state.model.State;

/**
 * 已支付状态
 * 用户完成支付后的状态
 */
public class PaidState implements State {

    @Override
    public void pay(OrderContext context) {
        System.out.println("操作失败：订单已支付，无需重复支付");
    }

    @Override
    public void ship(OrderContext context) {
        System.out.println("订单已发货");
        // 状态转换：已支付 -> 已发货
        context.setState(new ShippedState());
    }

    @Override
    public void receive(OrderContext context) {
        System.out.println("操作失败：订单尚未发货，不能收货");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("操作失败：订单已支付，不能直接取消，需要申请退款");
    }

    @Override
    public String getName() {
        return "已支付";
    }
}
