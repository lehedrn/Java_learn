package design_pattern.src.state.context;

import design_pattern.src.state.model.State;
import design_pattern.src.state.state.UnpaidState;

/**
 * 订单上下文
 * 持有当前状态对象的引用，并委托给状态对象处理请求
 */
public class OrderContext {

    private State state;
    private final String orderId;

    public OrderContext(String orderId) {
        this.orderId = orderId;
        // 订单创建时默认为待支付状态
        this.state = new UnpaidState();
    }

    /**
     * 执行支付操作
     */
    public void pay() {
        System.out.printf("[订单 %s] 当前状态：%s\n", orderId, state.getName());
        state.pay(this);
        System.out.printf("[订单 %s] 新状态：%s\n\n", orderId, state.getName());
    }

    /**
     * 执行发货操作
     */
    public void ship() {
        System.out.printf("[订单 %s] 当前状态：%s\n", orderId, state.getName());
        state.ship(this);
        System.out.printf("[订单 %s] 新状态：%s\n\n", orderId, state.getName());
    }

    /**
     * 执行收货操作
     */
    public void receive() {
        System.out.printf("[订单 %s] 当前状态：%s\n", orderId, state.getName());
        state.receive(this);
        System.out.printf("[订单 %s] 新状态：%s\n\n", orderId, state.getName());
    }

    /**
     * 执行取消操作
     */
    public void cancel() {
        System.out.printf("[订单 %s] 当前状态：%s\n", orderId, state.getName());
        state.cancel(this);
        System.out.printf("[订单 %s] 新状态：%s\n\n", orderId, state.getName());
    }

    /**
     * 获取当前状态
     * @return 当前状态对象
     */
    public State getState() {
        return state;
    }

    /**
     * 设置新状态
     * @param state 新的状态对象
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * 获取订单 ID
     * @return 订单 ID
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 获取当前状态名称
     * @return 状态名称
     */
    public String getStateName() {
        return state.getName();
    }
}
