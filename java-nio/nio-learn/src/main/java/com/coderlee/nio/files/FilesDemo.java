package com.coderlee.nio.files;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Java NIO Files工具类演示
 * 演示了NIO中Files类的各种文件操作功能
 * 包括：创建目录、复制文件、移动文件、查找文件、删除文件等操作
 * 使用SimpleFileVisitor实现文件树遍历
 */
@Slf4j
public class FilesDemo {

    /**
     * 程序入口点
     * 演示完整的文件操作流程：
     * 1. 创建目录
     * 2. 复制文件到新目录
     * 3. 移动并重命名文件
     * 4. 在项目中查找指定文件
     * 5. 删除整个目录及其内容
     * 
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        // 获取项目根目录路径
        String projectRoot = System.getProperty("user.dir");
        // 构建目标目录路径
        Path path = Paths.get(projectRoot, "java-nio", "nio-learn", "src", "main", "resources", "files_data");
        
        // 执行各种文件操作演示
        createDirectory(path);
        copyFile(Paths.get(projectRoot, "java-nio", "nio-learn", "src", "main", "resources", "file1.text"), path.resolve("file1.text"));
        moveFile(path.resolve("file1.text"), path.resolve("file1-rename.text"));
        findFile(projectRoot, "file1-rename.text");
        deleteFile(path);
    }

    /**
     * 递归删除文件或目录
     * 如果是目录则遍历删除所有子文件和子目录
     * 如果是文件则直接删除
     * 
     * @param path 要删除的文件或目录路径
     * @throws RuntimeException 当删除操作失败时抛出
     */
    private static void deleteFile(Path path) {
        try {
            // 判断是否为目录
            if (Files.isDirectory(path)) {
                // 使用walkFileTree遍历文件树进行递归删除
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    /**
                     * 访问文件时的处理逻辑
                     * @param file 当前访问的文件路径
                     * @param attrs 文件基本属性
                     * @return 继续遍历
                     * @throws IOException IO异常
                     */
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file); // 删除文件
                        log.info("delete file: {}", file.getFileName()); // 记录日志
                        return FileVisitResult.CONTINUE; // 继续遍历
                    }

                    /**
                     * 访问完目录后的处理逻辑
                     * @param dir 当前访问的目录路径
                     * @param exc 遍历过程中可能发生的异常
                     * @return 继续遍历或终止
                     * @throws IOException IO异常
                     */
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        if (null == exc) {
                            Files.delete(dir); // 删除空目录
                            log.info("delete directory: {}", dir.getFileName()); // 记录日志
                            return FileVisitResult.CONTINUE; // 继续遍历
                        } else {
                            throw exc; // 抛出异常
                        }
                    }
                });
            } else {
                // 直接删除单个文件
                Files.delete(path);
                log.info("delete file: {}", path.getFileName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 在指定目录树中查找文件
     * 使用深度优先搜索遍历目录结构
     * 找到目标文件后立即终止搜索
     * 
     * @param rootPath 搜索的根目录路径
     * @param fileName 要查找的文件名
     * @throws RuntimeException 当搜索过程发生IO异常时抛出
     */
    private static void findFile(String rootPath, String fileName) {
        try {
            // 获取根路径对象
            Path root = Paths.get(rootPath);
            // 遍历文件树进行搜索
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                // 当前遍历的目录深度
                private int currentDepth = 0;

                /**
                 * 访问目录前的处理逻辑
                 * 用于记录当前遍历的目录层级
                 * @param dir 当前访问的目录路径
                 * @param attrs 目录基本属性
                 * @return 继续遍历
                 * @throws IOException IO异常
                 */
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    // 计算相对于根目录的深度
                    int depth = root.relativize(dir).getNameCount();
                    // 当进入新的层级时记录日志
                    if (depth != currentDepth) {
                        currentDepth = depth;
                        log.info("开始查找第 {} 层目录: {}", depth, dir.toAbsolutePath());
                    }
                    return FileVisitResult.CONTINUE; // 继续遍历
                }

                /**
                 * 访问文件时的处理逻辑
                 * 检查是否为目标文件
                 * @param file 当前访问的文件路径
                 * @param attrs 文件基本属性
                 * @return 继续遍历或终止
                 * @throws IOException IO异常
                 */
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileString = file.toAbsolutePath().toString();
                    // 检查文件名是否匹配
                    if (fileString.endsWith(fileName)) {
                        log.info("在第 {} 层找到文件: {}", currentDepth, file.toAbsolutePath());
                        return FileVisitResult.TERMINATE; // 找到目标文件，终止搜索
                    }
                    return FileVisitResult.CONTINUE; // 继续搜索
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 移动文件并可选择性重命名
     * 将源文件移动到目标位置
     * 
     * @param source 源文件路径
     * @param target 目标文件路径
     * @throws RuntimeException 当移动操作失败时抛出
     */
    private static void moveFile(Path source, Path target) {
        try {
            // 执行文件移动操作
            Files.move(source, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 复制文件从源路径到目标路径
     * 默认情况下不会覆盖已存在的文件
     * 
     * @param source 源文件路径
     * @param target 目标文件路径
     * @throws RuntimeException 当复制操作失败时抛出
     */
    private static void copyFile(Path source, Path target) {
        try {
            // 复制文件（不覆盖已存在文件）
            Files.copy(source, target);
            // 覆盖选项被注释掉，如需启用可取消注释下面这行
            // Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建单个目录
     * 注意：只会创建最后一级目录，如果父目录不存在会抛出异常
     * 
     * @param path 要创建的目录路径
     * @throws RuntimeException 当创建目录失败时抛出
     */
    private static void createDirectory(Path path) {
        try {
            // 创建目录
            Path filesData = Files.createDirectory(path);
            log.info("filesData: {}", filesData); // 记录创建成功的目录信息
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
