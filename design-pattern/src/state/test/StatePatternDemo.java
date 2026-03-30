package design_pattern.src.state.test;

import design_pattern.src.state.context.OrderContext;

/**
 * 状态模式测试客户端
 * 演示订单状态在不同操作下的转换
 */
public class StatePatternDemo {

    public static void main(String[] args) {
        System.out.println("========== 状态模式演示：订单系统 ==========\n");

        // 场景 1：正常流程 - 支付 -> 发货 -> 收货
        System.out.println("【场景 1】正常购物流程");
        System.out.println("------------------------------------------");
        OrderContext order1 = new OrderContext("ORDER-001");
        order1.pay();      // 待支付 -> 已支付
        order1.ship();     // 已支付 -> 已发货
        order1.receive();  // 已发货 -> 已完成
        // 尝试在已完成状态下进行操作
        order1.pay();
        order1.cancel();

        // 场景 2：用户取消订单
        System.out.println("【场景 2】用户取消订单");
        System.out.println("------------------------------------------");
        OrderContext order2 = new OrderContext("ORDER-002");
        order2.cancel();   // 待支付 -> 已取消
        // 尝试在已取消状态下进行操作
        order2.pay();
        order2.ship();

        // 场景 3：支付后取消（需要退款）
        System.out.println("【场景 3】支付后申请取消");
        System.out.println("------------------------------------------");
        OrderContext order3 = new OrderContext("ORDER-003");
        order3.pay();      // 待支付 -> 已支付
        order3.cancel();   // 提示需要退款

        // 场景 4：发货后退货
        System.out.println("【场景 4】发货后申请退货");
        System.out.println("------------------------------------------");
        OrderContext order4 = new OrderContext("ORDER-004");
        order4.pay();      // 待支付 -> 已支付
        order4.ship();     // 已支付 -> 已发货
        order4.cancel();   // 提示需要退货

        // 场景 5：并发查看订单状态
        System.out.println("【场景 5】多个订单并行处理");
        System.out.println("------------------------------------------");
        OrderContext[] orders = {
            new OrderContext("ORDER-A"),
            new OrderContext("ORDER-B"),
            new OrderContext("ORDER-C")
        };
        orders[0].pay();
        orders[1].ship();   // 未支付不能发货
        orders[2].cancel();
        orders[0].ship();   // 已支付后可以发货
        orders[0].receive();// 已发货后可以收货

        System.out.println("========== 演示结束 ==========");
    }
}
