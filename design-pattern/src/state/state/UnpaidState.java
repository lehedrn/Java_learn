package design_pattern.src.state.state;

import design_pattern.src.state.context.OrderContext;
import design_pattern.src.state.model.State;

/**
 * 待支付状态
 * 订单创建后的初始状态
 */
public class UnpaidState implements State {

    @Override
    public void pay(OrderContext context) {
        System.out.println("订单支付成功");
        // 状态转换：待支付 -> 已支付
        context.setState(new PaidState());
    }

    @Override
    public void ship(OrderContext context) {
        System.out.println("操作失败：订单尚未支付，不能发货");
    }

    @Override
    public void receive(OrderContext context) {
        System.out.println("操作失败：订单尚未发货，不能收货");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("订单已取消");
        // 状态转换：待支付 -> 已取消
        context.setState(new CancelledState());
    }

    @Override
    public String getName() {
        return "待支付";
    }
}
