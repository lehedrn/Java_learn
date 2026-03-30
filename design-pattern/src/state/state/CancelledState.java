package design_pattern.src.state.state;

import design_pattern.src.state.context.OrderContext;
import design_pattern.src.state.model.State;

/**
 * 已取消状态
 * 订单被取消后的终态
 */
public class CancelledState implements State {

    @Override
    public void pay(OrderContext context) {
        System.out.println("操作失败：订单已取消，不能支付");
    }

    @Override
    public void ship(OrderContext context) {
        System.out.println("操作失败：订单已取消，不能发货");
    }

    @Override
    public void receive(OrderContext context) {
        System.out.println("操作失败：订单已取消，不能收货");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("操作失败：订单已取消，不能重复取消");
    }

    @Override
    public String getName() {
        return "已取消";
    }
}
