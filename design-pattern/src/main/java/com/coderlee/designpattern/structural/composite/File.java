package com.coderlee.designpattern.structural.composite;

/**
 * 叶子节点：文件
 * <p>
 * 文件是文件系统中的叶子节点，不能包含其他节点
 * </p>
 *
 * @author coderlee
 */
public class File implements FileSystemNode {

    private String name;
    private long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public void show(String indent) {
        System.out.println(indent + "📄 " + name + " (" + size + " B)");
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public void search(String name) {
        if (this.name.equals(name)) {
            System.out.println("找到文件：" + name);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
