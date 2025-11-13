package com.coderlee.concurrent.design.master.slave.right;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

/**
 * 自定义实现的 {@link java.util.Enumeration} 接口，
 * 用于将一组日志文件名转换为对应的输入流序列。
 *
 * @author coderlee
 */
public class HotGoodsEnumeration implements Enumeration<InputStream> {

    /**
     * 文件名迭代器
     */
    private Iterator<String> iterator;

    /**
     * 日志文件根路径
     */
    private String fileRoot;

    /**
     * 构造一个枚举对象，根据指定的日志文件名集合和根路径初始化。
     *
     * @param fileNames 需要打开的文件名集合
     * @param fileRoot  文件所在的根目录路径
     */
    public HotGoodsEnumeration(Set<String> fileNames, String fileRoot) {
        this.fileRoot = fileRoot;
        this.iterator = fileNames.iterator();
    }

    /**
     * 判断是否还有下一个元素（即是否存在未被读取的文件）
     *
     * @return 如果存在更多文件则返回 true，否则返回 false
     */
    @Override
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    /**
     * 获取当前文件名对应的内容输入流。
     * 若无法找到文件，则抛出运行时异常。
     *
     * @return 下一个文件的输入流
     * @throws RuntimeException 如果文件不存在或发生其他IO错误
     */
    @Override
    public InputStream nextElement() {
        String fileName = iterator.next();
        InputStream in = null;
        try {
            in = new FileInputStream(fileRoot + fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return in;
    }
}

