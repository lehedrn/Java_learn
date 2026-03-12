package com.coderlee.designpattern.behavioral.template.callback;

/**
 * 抽象类：文件上传模板
 * <p>
 * 定义文件上传的标准流程，通过回调接口报告进度
 * </p>
 *
 * @author coderlee
 */
public abstract class FileUploader {

    /**
     * 最大分片大小（1MB）
     */
    protected static final long CHUNK_SIZE = 1024 * 1024;

    /**
     * 模板方法：上传文件的完整流程
     */
    public final void upload(String filePath, UploadCallback callback) {
        // 回调：开始
        callback.onStart();

        // 验证文件
        if (!validateFile(filePath)) {
            callback.onFailure(400, "文件验证失败");
            return;
        }

        // 获取文件大小
        long fileSize = getFileSize(filePath);

        // 分片上传
        long uploadedSize = 0;
        int chunkIndex = 0;

        while (uploadedSize < fileSize) {
            // 计算当前分片大小
            long currentChunkSize = Math.min(CHUNK_SIZE, fileSize - uploadedSize);

            // 上传分片（回调进度）
            uploadChunk(filePath, chunkIndex, currentChunkSize);

            uploadedSize += currentChunkSize;
            chunkIndex++;

            // 回调：进度
            int progress = (int) (uploadedSize * 100 / fileSize);
            callback.onProgress(progress, uploadedSize, fileSize);

            // 模拟网络延迟
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                callback.onFailure(500, "上传中断");
                return;
            }
        }

        // 合并分片
        String fileId = mergeChunks(filePath, chunkIndex);

        // 回调：成功
        String url = generateUrl(fileId);
        callback.onSuccess(fileId, url);
    }

    /**
     * 具体方法：验证文件
     */
    protected boolean validateFile(String filePath) {
        System.out.println("🔍 验证文件：" + filePath);
        // 模拟验证
        return filePath != null && !filePath.isEmpty();
    }

    /**
     * 抽象方法：获取文件大小
     */
    protected abstract long getFileSize(String filePath);

    /**
     * 抽象方法：上传分片
     */
    protected abstract void uploadChunk(String filePath, int chunkIndex, long chunkSize);

    /**
     * 抽象方法：合并分片
     */
    protected abstract String mergeChunks(String filePath, int chunkCount);

    /**
     * 具体方法：生成访问 URL
     */
    protected String generateUrl(String fileId) {
        return "https://cdn.example.com/files/" + fileId;
    }
}
