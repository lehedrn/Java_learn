package com.coderlee.concurrent.design.pc.wrong.service;

/**
 * 文件上传服务接口
 *
 * <p>定义了上传文件的操作规范。</p>
 */
public interface UploadService {

    /**
     * 上传一个或多个文件
     *
     * @param fileNames 要上传的文件名数组
     */
    void upload(String... fileNames);
}
