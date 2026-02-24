package com.coderlee.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * FileChannel 文件传输操作演示类
 * 展示了 FileChannel 的 transferFrom 和 transferTo 方法的使用
 * 这两个方法用于在不同的文件通道之间高效地传输数据
 * 
 * @author coderlee
 * @since 1.0
 */
@Slf4j
public class FileChannelDemo3 {

    /**
     * 测试 transferFrom 方法
     * 将数据从源通道传输到目标通道
     * 
     * 该方法演示了如何使用 transferFrom 方法：
     * 1. 打开源文件 file1.text（只读）
     * 2. 打开目标文件 file3.text（读写）
     * 3. 使用 transferFrom 方法将源文件内容传输到目标文件
     * 
     * transferFrom 特点：
     * - 数据传输方向：从参数中的源通道传输到当前通道
     * - 可以指定传输的起始位置和字节数
     * - 操作系统可能使用零拷贝技术优化性能
     */
    @Test
    public void testTransferFrom() {
        try (
            RandomAccessFile aFile = new RandomAccessFile(FileChannelDemo1.class.getResource("/file1.text").toURI().getPath(), "rw");
            RandomAccessFile bFile = new RandomAccessFile(System.getProperty("user.dir") + "/src/main/resources/file3.text", "rw")) {
                FileChannel fromChannel = aFile.getChannel();
                FileChannel toChannel = bFile.getChannel();

                long position = 0;
                long count = fromChannel.size();

                // transferFrom(源通道, 目标位置, 传输字节数)
                // 将 fromChannel 的数据传输到 toChannel 的 position 位置
                toChannel.transferFrom(fromChannel, position, count);
        } catch (URISyntaxException | IOException e) {
            log.error("transferFrom 操作失败", e);
        }
    }

    /**
     * 测试 transferTo 方法
     * 将数据从当前通道传输到目标通道
     * 
     * 该方法演示了如何使用 transferTo 方法：
     * 1. 打开源文件 file3.text（只读）
     * 2. 打开目标文件 file4.text（读写）
     * 3. 使用 transferTo 方法将源文件内容传输到目标文件
     * 
     * transferTo 特点：
     * - 数据传输方向：从当前通道传输到参数中的目标通道
     * - 可以指定传输的起始位置和字节数
     * - 操作系统可能使用零拷贝技术优化性能
     * - 在某些操作系统上可能比 transferFrom 更高效
     */
    @Test
    public void testTransferTo() {
        try (
                RandomAccessFile aFile = new RandomAccessFile(FileChannelDemo3.class.getResource("/file3.text").toURI().getPath(), "rw");
                RandomAccessFile bFile = new RandomAccessFile(System.getProperty("user.dir") + "/src/main/resources/file4.text", "rw")) {
            FileChannel fromChannel = aFile.getChannel();
            FileChannel toChannel = bFile.getChannel();

            long position = 0;
            long count = fromChannel.size();

            // transferTo(源位置, 传输字节数, 目标通道)
            // 将 fromChannel 的 position 位置开始的数据传输到 toChannel
            fromChannel.transferTo(position, count, toChannel);
        } catch (URISyntaxException | IOException e) {
            log.error("transferTo 操作失败", e);
        }
    }
    
    /**
     * transferFrom vs transferTo 对比说明：
     * 
     * 相同点：
     * 1. 都用于在 FileChannel 之间传输数据
     * 2. 都支持指定传输的位置和字节数
     * 3. 都可能利用操作系统的零拷贝机制提高性能
     * 4. 返回实际传输的字节数
     * 
     * 不同点：
     * transferFrom：
     * - 调用方式：目标通道.transferFrom(源通道, 位置, 字节数)
     * - 数据流向：参数通道 → 当前通道
     * 
     * transferTo：
     * - 调用方式：源通道.transferTo(位置, 字节数, 目标通道)
     * - 数据流向：当前通道 → 参数通道
     * - 在某些 Unix 系统上可能有更好性能
     */
}
