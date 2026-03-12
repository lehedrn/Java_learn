package com.coderlee.designpattern.behavioral.template.callback;

/**
 * 具体实现：文档文件上传器
 *
 * @author coderlee
 */
public class DocumentUploader extends FileUploader {

    @Override
    protected long getFileSize(String filePath) {
        // 模拟获取文档大小
        System.out.println("📊 获取文档大小：" + filePath);
        return 2 * 1024 * 1024; // 2MB
    }

    @Override
    protected void uploadChunk(String filePath, int chunkIndex, long chunkSize) {
        System.out.println("⬆️  上传文档分片 [" + chunkIndex + "]: " + chunkSize + " 字节");
    }

    @Override
    protected String mergeChunks(String filePath, int chunkCount) {
        System.out.println("🔗 合并文档分片，共 " + chunkCount + " 片");
        return "DOC-" + System.currentTimeMillis();
    }

    @Override
    protected String generateUrl(String fileId) {
        return "https://docs.example.com/files/" + fileId;
    }
}
