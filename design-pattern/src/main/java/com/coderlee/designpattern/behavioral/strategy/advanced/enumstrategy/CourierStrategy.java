package com.coderlee.designpattern.behavioral.strategy.advanced.enumstrategy;

/**
 * 枚举实现策略接口：快递公司
 * <p>
 * 使用枚举来实现策略接口，更加简洁
 * 每个枚举常量是一个独立的策略
 * </p>
 *
 * @author coderlee
 */
public enum CourierStrategy implements ShippingStrategy {

    /**
     * 顺丰速运：速度快，价格高
     * 计费规则：首重（1kg）15 元，续重 5 元/kg，远程附加费
     */
    SF_EXPRESS("顺丰速运") {
        @Override
        public double calculateShippingFee(double weight, double distance) {
            // 首重 1kg 15 元
            double baseFee = 15.0;
            // 续重费用
            double additionalWeight = Math.max(0, weight - 1);
            double additionalFee = additionalWeight * 5;
            // 远程附加费（超过 500km）
            double distanceFee = distance > 500 ? (distance - 500) * 0.01 : 0;
            double total = baseFee + additionalFee + distanceFee;
            System.out.printf("   【%s】首重￥15 + 续重￥%.2f + 远程费￥%.2f = ￥%.2f\n",
                    getCourierName(), additionalFee, distanceFee, total);
            return total;
        }

        @Override
        public int estimateDeliveryDays(double distance) {
            if (distance < 200) return 1;
            if (distance < 500) return 2;
            return 3;
        }
    },

    /**
     * 中通快递：价格适中，速度一般
     * 计费规则：首重（1kg）10 元，续重 3 元/kg
     */
    ZTO_EXPRESS("中通快递") {
        @Override
        public double calculateShippingFee(double weight, double distance) {
            // 首重 1kg 10 元
            double baseFee = 10.0;
            // 续重费用
            double additionalWeight = Math.max(0, weight - 1);
            double additionalFee = additionalWeight * 3;
            // 远程附加费（超过 800km）
            double distanceFee = distance > 800 ? (distance - 800) * 0.005 : 0;
            double total = baseFee + additionalFee + distanceFee;
            System.out.printf("   【%s】首重￥10 + 续重￥%.2f + 远程费￥%.2f = ￥%.2f\n",
                    getCourierName(), additionalFee, distanceFee, total);
            return total;
        }

        @Override
        public int estimateDeliveryDays(double distance) {
            if (distance < 300) return 2;
            if (distance < 800) return 3;
            return 4;
        }
    },

    /**
     * 邮政平邮：价格最低，速度慢
     * 计费规则：统一 5 元 + 1 元/kg
     */
    CHINA_POST("邮政平邮") {
        @Override
        public double calculateShippingFee(double weight, double distance) {
            // 基础费用 5 元
            double baseFee = 5.0;
            // 重量费用
            double weightFee = weight * 1;
            double total = baseFee + weightFee;
            System.out.printf("   【%s】基础￥5 + 重量￥%.2f = ￥%.2f\n",
                    getCourierName(), weightFee, total);
            return total;
        }

        @Override
        public int estimateDeliveryDays(double distance) {
            if (distance < 200) return 3;
            if (distance < 500) return 5;
            return 7;
        }
    },

    /**
     * 京东物流：自营商品使用
     * 计费规则：满 99 包邮，否则 6 元基础费 + 2 元/kg
     */
    JD_LOGISTICS("京东物流") {
        @Override
        public double calculateShippingFee(double weight, double distance) {
            // 京东通常满 99 包邮，这里简化计算
            double baseFee = 6.0;
            double weightFee = weight * 2;
            double total = baseFee + weightFee;
            System.out.printf("   【%s】基础￥6 + 重量￥%.2f = ￥%.2f\n",
                    getCourierName(), weightFee, total);
            return total;
        }

        @Override
        public int estimateDeliveryDays(double distance) {
            // 京东物流通常次日达
            if (distance < 500) return 1;
            return 2;
        }
    };

    /**
     * 快递公司名称
     */
    private final String courierName;

    CourierStrategy(String courierName) {
        this.courierName = courierName;
    }

    @Override
    public String getCourierName() {
        return courierName;
    }
}
