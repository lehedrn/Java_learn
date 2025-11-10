package com.coderlee.guarded.suspension.lock.alarm.action;

import com.coderlee.guarded.suspension.lock.alarm.predicate.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.Callable;

/**
 * 受保护动作抽象类
 * <p>
 * 定义了一个受保护的动作，只有当特定条件满足时才能执行。
 * 实现了 {@link java.util.concurrent.Callable} 接口，支持返回值。
 *
 * @param <V> 动作执行后的返回值类型
 * @see com.coderlee.guarded.suspension.lock.alarm.predicate.Predicate
 * @see java.util.concurrent.Callable
 */
@Getter
@AllArgsConstructor
public abstract class GuardedAction<V> implements Callable<V> {

    /**
     * 条件谓词，决定动作是否可以执行
     */
    private final Predicate predicate;

}
