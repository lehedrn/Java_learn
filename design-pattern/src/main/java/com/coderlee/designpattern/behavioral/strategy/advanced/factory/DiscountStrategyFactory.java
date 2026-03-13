package com.coderlee.designpattern.behavioral.strategy.advanced.factory;

import java.util.EnumMap;
import java.util.Map;

/**
 * 工厂类：折扣策略工厂
 * <p>
 * 根据会员等级自动创建对应的折扣策略
 * 将策略的创建与使用分离
 * </p>
 *
 * @author coderlee
 */
public class DiscountStrategyFactory {

    /**
     * 会员等级枚举
     */
    public enum MemberLevel {
        /** 普通会员 */
        NORMAL,
        /** VIP 会员 */
        VIP,
        /** 超级 VIP */
        SUPER_VIP
    }

    /**
     * 策略缓存（单例模式，避免重复创建）
     */
    private static final Map<MemberLevel, DiscountStrategy> strategyCache = new EnumMap<>(MemberLevel.class);

    static {
        // 预创建所有策略（单例）
        strategyCache.put(MemberLevel.NORMAL, new NormalMemberDiscount());
        strategyCache.put(MemberLevel.VIP, new VipMemberDiscount());
        strategyCache.put(MemberLevel.SUPER_VIP, new SuperVipMemberDiscount());
    }

    /**
     * 根据会员等级获取折扣策略
     *
     * @param level 会员等级
     * @return 对应的折扣策略
     */
    public static DiscountStrategy getStrategy(MemberLevel level) {
        if (level == null) {
            return new NoDiscount();
        }

        DiscountStrategy strategy = strategyCache.get(level);
        if (strategy == null) {
            return new NoDiscount();
        }

        return strategy;
    }

    /**
     * 获取所有可用的会员等级
     *
     * @return 会员等级数组
     */
    public static MemberLevel[] getAllLevels() {
        return MemberLevel.values();
    }
}
