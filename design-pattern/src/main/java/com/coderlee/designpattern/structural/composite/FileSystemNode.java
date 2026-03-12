package com.coderlee.designpattern.structural.composite;

/**
 * 组件接口：文件系统中的节点
 * <p>
 * 定义了文件和目录的公共行为
 * </p>
 *
 * @author coderlee
 */
public interface FileSystemNode {
    /**
     * 显示文件/目录信息
     * @param indent 缩进
     */
    void show(String indent);

    /**
     * 获取大小（字节）
     * @return 大小
     */
    long getSize();

    /**
     * 搜索文件
     * @param name 文件名
     */
    void search(String name);

    /**
     * 添加节点（叶子节点不支持）
     * @param node 节点
     */
    default void add(FileSystemNode node) {
        throw new UnsupportedOperationException("叶子节点不支持添加操作");
    }

    /**
     * 移除节点（叶子节点不支持）
     * @param node 节点
     */
    default void remove(FileSystemNode node) {
        throw new UnsupportedOperationException("叶子节点不支持移除操作");
    }

    /**
     * 获取子节点（叶子节点不支持）
     * @param index 索引
     * @return 节点
     */
    default FileSystemNode getChild(int index) {
        throw new UnsupportedOperationException("叶子节点不支持获取子节点操作");
    }
}
