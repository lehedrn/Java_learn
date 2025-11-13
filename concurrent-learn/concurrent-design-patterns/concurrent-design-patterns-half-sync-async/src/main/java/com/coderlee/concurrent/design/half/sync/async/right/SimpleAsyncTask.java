package com.coderlee.concurrent.design.half.sync.async.right;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 简单异步任务实现类。
 * <p>
 * 继承自 {@link AsyncTask}，实现了发送短信的功能。
 * </p>
 */
@Slf4j
public class SimpleAsyncTask extends AsyncTask<String> {

    /**
     * 实际执行异步任务的方法。
     * <p>
     * 这里模拟发送短信的过程。
     * </p>
     *
     * @param params 参数列表，第一个参数应为手机号码
     * @return 发送结果字符串
     */
    @Override
    protected String doExecute(Object... params) {
        log.info("发送短信, 传递的手机号为: {}", params[0]);
        try {
            TimeUnit.SECONDS.sleep(2); // 模拟发送短信耗时
        } catch (InterruptedException e) {
            log.error("发送短信发生异常", e);
        }
        return "发送短信成功";
    }

    /**
     * 在异步任务执行前调用的方法。
     * <p>
     * 记录即将发送短信的日志信息。
     * </p>
     *
     * @param params 参数列表，第一个参数应为手机号码
     */
    @Override
    protected void doPreExecute(Object... params) {
        log.info("准备发送短信, 传递的手机号为: {}", params[0]);
    }

    /**
     * 对外暴露的执行方法。
     * <p>
     * 检查手机号是否有效，然后调用父类的dispatch方法执行异步任务。
     * </p>
     *
     * @param phone 手机号码
     * @return 返回一个Future对象，可用于获取异步任务的执行结果
     */
    public Future<String> execute(String phone) {
        if (null == phone || phone.isEmpty()) {
            throw new IllegalArgumentException("手机号不能为空");
        }
        return this.dispatch(phone);
    }
}

