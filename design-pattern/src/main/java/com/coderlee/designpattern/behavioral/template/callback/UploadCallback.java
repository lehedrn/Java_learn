package com.coderlee.designpattern.behavioral.template.callback;

/**
 * 上传进度回调接口
 * <p>
 * 用于在文件上传过程中报告进度和结果
 * </p>
 *
 * @author coderlee
 */
public interface UploadCallback {
    /**
     * 上传开始
     */
    void onStart();

    /**
     * 上传进度
     *
     * @param progress 进度百分比 (0-100)
     * @param uploadedSize 已上传大小（字节）
     * @param totalSize 总大小（字节）
     */
    void onProgress(int progress, long uploadedSize, long totalSize);

    /**
     * 上传成功
     *
     * @param fileId 文件 ID
     * @param url 文件访问 URL
     */
    void onSuccess(String fileId, String url);

    /**
     * 上传失败
     *
     * @param errorCode 错误码
     * @param message 错误信息
     */
    void onFailure(int errorCode, String message);
}
