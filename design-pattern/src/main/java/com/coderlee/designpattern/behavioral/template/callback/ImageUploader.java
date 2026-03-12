package com.coderlee.designpattern.behavioral.template.callback;

/**
 * 具体实现：图片文件上传器
 *
 * @author coderlee
 */
public class ImageUploader extends FileUploader {

    @Override
    protected long getFileSize(String filePath) {
        // 模拟获取图片大小
        System.out.println("📊 获取图片大小：" + filePath);
        return 5 * 1024 * 1024; // 5MB
    }

    @Override
    protected void uploadChunk(String filePath, int chunkIndex, long chunkSize) {
        System.out.println("⬆️  上传图片分片 [" + chunkIndex + "]: " + chunkSize + " 字节");
    }

    @Override
    protected String mergeChunks(String filePath, int chunkCount) {
        System.out.println("🔗 合并图片分片，共 " + chunkCount + " 片");
        return "IMG-" + System.currentTimeMillis();
    }
}
