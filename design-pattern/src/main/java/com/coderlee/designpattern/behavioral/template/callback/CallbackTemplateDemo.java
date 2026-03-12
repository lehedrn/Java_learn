package com.coderlee.designpattern.behavioral.template.callback;

/**
 * 带回调的模板方法演示 - 文件上传
 * <p>
 * 演示场景：
 * 1. 文件上传系统支持多种文件类型
 * 2. 上传过程中通过回调报告进度
 * 3. 上传完成后回调通知结果
 * </p>
 *
 * @author coderlee
 */
public class CallbackTemplateDemo {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     模板方法模式 - 带回调的模板方法               ║");
        System.out.println("║     场景：文件上传系统                           ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        // 创建上传回调
        UploadCallback callback = new UploadCallback() {
            @Override
            public void onStart() {
                System.out.println("🚀 上传开始...\n");
            }

            @Override
            public void onProgress(int progress, long uploadedSize, long totalSize) {
                System.out.println("📈 进度：" + progress + "% (" + uploadedSize / 1024 + "KB / " + totalSize / 1024 + "KB)");
            }

            @Override
            public void onSuccess(String fileId, String url) {
                System.out.println("✅ 上传成功！");
                System.out.println("   文件 ID: " + fileId);
                System.out.println("   访问 URL: " + url);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                System.out.println("❌ 上传失败！");
                System.out.println("   错误码：" + errorCode);
                System.out.println("   错误信息：" + message);
            }
        };

        // 上传图片
        System.out.println("===== 上传图片 =====");
        FileUploader imageUploader = new ImageUploader();
        imageUploader.upload("photo.jpg", callback);

        System.out.println("\n\n===== 上传视频 =====");
        FileUploader videoUploader = new VideoUploader();
        videoUploader.upload("movie.mp4", callback);

        System.out.println("\n\n===== 上传文档 =====");
        FileUploader documentUploader = new DocumentUploader();
        documentUploader.upload("report.pdf", callback);

        System.out.println("\n========== 演示结束 ==========");
    }
}
