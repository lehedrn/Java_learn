package com.coderlee.designpattern.behavioral.template.callback;

/**
 * 具体实现：视频文件上传器
 *
 * @author coderlee
 */
public class VideoUploader extends FileUploader {

    @Override
    protected long getFileSize(String filePath) {
        // 模拟获取视频大小
        System.out.println("📊 获取视频大小：" + filePath);
        return 50 * 1024 * 1024; // 50MB
    }

    @Override
    protected void uploadChunk(String filePath, int chunkIndex, long chunkSize) {
        System.out.println("⬆️  上传视频分片 [" + chunkIndex + "]: " + chunkSize + " 字节");
    }

    @Override
    protected String mergeChunks(String filePath, int chunkCount) {
        System.out.println("🔗 合并视频分片，共 " + chunkCount + " 片");
        return "VID-" + System.currentTimeMillis();
    }
}
