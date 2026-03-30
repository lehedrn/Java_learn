package design_pattern.src.state.state;

import design_pattern.src.state.context.OrderContext;
import design_pattern.src.state.model.State;

/**
 * 已完成状态
 * 用户确认收货后的终态
 */
public class CompletedState implements State {

    @Override
    public void pay(OrderContext context) {
        System.out.println("操作失败：订单已完成，无需支付");
    }

    @Override
    public void ship(OrderContext context) {
        System.out.println("操作失败：订单已完成，不能发货");
    }

    @Override
    public void receive(OrderContext context) {
        System.out.println("操作失败：订单已完成，不能重复收货");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("操作失败：订单已完成，不能取消");
    }

    @Override
    public String getName() {
        return "已完成";
    }
}
