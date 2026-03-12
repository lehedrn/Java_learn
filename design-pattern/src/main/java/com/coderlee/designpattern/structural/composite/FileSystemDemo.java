package com.coderlee.designpattern.structural.composite;

/**
 * 文件系统演示类
 *
 * @author coderlee
 */
public class FileSystemDemo {

    public static void main(String[] args) {
        System.out.println("========== 组合模式演示 - 文件系统 ==========\n");

        // 创建文件
        File file1 = new File("简历.pdf", 1024 * 100);        // 100KB
        File file2 = new File("照片.jpg", 1024 * 500);         // 500KB
        File file3 = new File("文档.docx", 1024 * 50);         // 50KB
        File file4 = new File("表格.xlsx", 1024 * 80);         // 80KB
        File file5 = new File("代码.java", 1024 * 10);         // 10KB
        File file6 = new File("笔记.txt", 1024 * 5);           // 5KB

        // 创建文件夹
        Folder root = new Folder("根目录");
        Folder workFolder = new Folder("工作文档");
        Folder personalFolder = new Folder("个人资料");
        Folder codeFolder = new Folder("代码项目");
        Folder projectFolder = new Folder("项目 A");

        // 构建目录树
        root.add(file1);
        root.add(workFolder);
        root.add(personalFolder);

        workFolder.add(file3);
        workFolder.add(file4);
        workFolder.add(projectFolder);

        projectFolder.add(file5);
        projectFolder.add(file6);

        personalFolder.add(file2);

        System.out.println("--- 目录结构 ---");
        root.show("");

        System.out.println("\n--- 计算大小 ---");
        System.out.println("工作文档大小：" + workFolder.getSize() / 1024 + " KB");
        System.out.println("个人资料大小：" + personalFolder.getSize() / 1024 + " KB");
        System.out.println("根目录总大小：" + root.getSize() / 1024 + " KB");

        System.out.println("\n--- 搜索文件 ---");
        root.search("简历.pdf");
        root.search("代码项目");

        System.out.println("\n========== 演示结束 ==========");
    }
}
