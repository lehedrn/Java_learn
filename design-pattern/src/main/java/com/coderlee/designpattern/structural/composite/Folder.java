package com.coderlee.designpattern.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合节点：文件夹（目录）
 * <p>
 * 文件夹可以包含文件和其他文件夹
 * </p>
 *
 * @author coderlee
 */
public class Folder implements FileSystemNode {

    private String name;
    private List<FileSystemNode> children;

    public Folder(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    @Override
    public void show(String indent) {
        System.out.println(indent + "📁 " + name);
        for (FileSystemNode node : children) {
            node.show(indent + "    ");
        }
    }

    @Override
    public long getSize() {
        long totalSize = 0;
        for (FileSystemNode node : children) {
            totalSize += node.getSize();
        }
        return totalSize;
    }

    @Override
    public void search(String name) {
        if (this.name.equals(name)) {
            System.out.println("找到文件夹：" + name);
        }
        // 递归搜索子节点
        for (FileSystemNode node : children) {
            node.search(name);
        }
    }

    @Override
    public void add(FileSystemNode node) {
        children.add(node);
        System.out.println("添加 " + node + " 到 " + name);
    }

    @Override
    public void remove(FileSystemNode node) {
        children.remove(node);
        System.out.println("从 " + name + " 移除 " + node);
    }

    @Override
    public FileSystemNode getChild(int index) {
        return children.get(index);
    }

    public int getChildCount() {
        return children.size();
    }

    @Override
    public String toString() {
        return name + " (文件夹)";
    }
}
