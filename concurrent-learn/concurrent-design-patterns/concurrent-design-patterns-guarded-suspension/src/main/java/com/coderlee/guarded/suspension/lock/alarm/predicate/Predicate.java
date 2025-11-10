package com.coderlee.guarded.suspension.lock.alarm.predicate;

/**
 * 条件谓词接口
 * <p>
 * 函数式接口，用于评估某个条件是否满足。
 */
@FunctionalInterface
public interface Predicate {

    /**
     * 评估条件是否满足
     *
     * @return 条件满足返回true，否则返回false
     */
    boolean evaluate();
}
