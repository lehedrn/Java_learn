package com.coderlee.concurrent.learn.lab04;

import lombok.extern.slf4j.Slf4j;

/**
 * 回调接口的实现类，用于处理任务的返回结果。
 * <p>
 * 该类实现了 {@link TaskCallable} 接口，主要用于对任务执行结果进行业务逻辑处理。
 * 当前实现仅为演示目的，直接将任务结果数据返回，并记录日志以便调试和跟踪。
 * </p>
 */
@Slf4j
public class TaskHandler implements TaskCallable<TaskResult> {

    /**
     * 处理任务结果的方法。
     * <p>
     * 该方法接收任务执行结果对象 `t`，对其进行处理后返回。
     * 当前实现仅记录任务结果日志并直接返回结果对象，后续可根据业务需求扩展处理逻辑。
     * </p>
     *
     * @param t 任务执行结果对象，类型为 {@link TaskResult}
     * @return 处理后的任务结果对象，类型为 {@link TaskResult}
     */
    @Override
    public TaskResult callable(TaskResult t) {
        // 记录任务结果日志，便于调试和跟踪
        log.info("taskResult: {}", t);
        // TODO: 拿到结果数据后进一步处理（可根据实际业务需求扩展逻辑）
        return t;
    }
}
