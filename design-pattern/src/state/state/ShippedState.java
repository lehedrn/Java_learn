package design_pattern.src.state.state;

import design_pattern.src.state.context.OrderContext;
import design_pattern.src.state.model.State;

/**
 * 已发货状态
 * 商家发货后的状态
 */
public class ShippedState implements State {

    @Override
    public void pay(OrderContext context) {
        System.out.println("操作失败：订单已发货，无需支付");
    }

    @Override
    public void ship(OrderContext context) {
        System.out.println("操作失败：订单已发货，不能重复发货");
    }

    @Override
    public void receive(OrderContext context) {
        System.out.println("用户已确认收货");
        // 状态转换：已发货 -> 已完成
        context.setState(new CompletedState());
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("操作失败：订单已发货，不能取消，需要申请退货");
    }

    @Override
    public String getName() {
        return "已发货";
    }
}
