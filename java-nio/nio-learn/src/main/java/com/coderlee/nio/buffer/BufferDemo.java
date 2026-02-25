/**
 * NIO Buffer操作演示类
 * 展示了Java NIO中各种Buffer的使用方法和特性
 * 包括ByteBuffer、IntBuffer等不同类型的缓冲区操作
 */
package com.coderlee.nio.buffer;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

@Slf4j
public class BufferDemo {

    /**
     * 测试ByteBuffer的基本读取操作
     * 演示如何使用FileChannel配合ByteBuffer读取文件内容
     * 使用StandardCharsets.UTF_8进行字符解码
     */
    @Test
    public void testBuffer01() {
        try (RandomAccessFile aFile = new RandomAccessFile(BufferDemo.class.getResource("/file1.text").toURI().getPath(), "rw")) {
            // 获取文件通道
            FileChannel channel = aFile.getChannel();
            // 分配2048字节的ByteBuffer
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            // 从通道读取数据到缓冲区
            int bytesRead = channel.read(buffer);
            
            // 循环读取直到文件末尾
            while (bytesRead != -1) {
                // 切换缓冲区为读取模式（limit=position, position=0）
                buffer.flip();
                
                // 方式一：逐字符读取（已注释）
//                while (buffer.hasRemaining()) {
//                    log.info("{}", (char) buffer.get());
//                }
                
                // 方式二：使用UTF-8解码器一次性解码整个缓冲区内容
                log.info("{}", StandardCharsets.UTF_8.decode(buffer));
                
                // 清空缓冲区，准备下一次读取（position=0, limit=capacity）
                buffer.clear();
                // 继续从通道读取数据
                bytesRead = channel.read(buffer);
            }
        } catch (IOException | URISyntaxException e) {
            log.error("读取文件时发生错误", e);
        }
    }

    /**
     * 测试IntBuffer的基本操作
     * 演示整型缓冲区的写入和读取过程
     * 向缓冲区中放入2,4,6,8,10,12,14,16这些偶数
     */
    @Test
    public void testBuffer02() {
        // 分配容量为8的IntBuffer
        IntBuffer buffer = IntBuffer.allocate(8);
        
        // 向缓冲区中写入数据：2*(i+1)
        for (int i = 0; i < buffer.capacity(); i++) {
            int j = 2 * (i + 1);
            buffer.put(j);
        }
        
        // 切换为读取模式
        buffer.flip();
        
        // 依次读取并打印缓冲区中的所有整数值
        while (buffer.hasRemaining()) {
            int value = buffer.get();
            log.info("value: {}", value);
        }
    }

    /**
     * 测试ByteBuffer的slice()方法
     * 演示如何创建缓冲区的子视图（切片）
     * 对切片的修改会反映到原缓冲区中
     */
    @Test
    public void testBuffer03() {
        // 分配10字节的ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        
        // 填充缓冲区：0,1,2,3,4,5,6,7,8,9
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        
        // 设置position为3，limit为7，创建一个[3,7)范围的视图
        buffer.position(3);
        buffer.limit(7);
        // 创建当前position到limit之间的切片缓冲区
        ByteBuffer slice = buffer.slice();
        
        // 对切片缓冲区中的每个元素乘以10
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 10;
            slice.put(i, b);
        }
        
        // 重置原缓冲区的位置和限制，准备读取全部内容
        buffer.position(0);
        buffer.limit(buffer.capacity());
        
        // 打印修改后的完整缓冲区内容
        while (buffer.remaining() > 0) {
            log.info("{}", buffer.get());
        }
    }

    /**
     * 测试ByteBuffer的只读视图功能
     * 演示asReadOnlyBuffer()方法的使用
     * 只读缓冲区不允许修改数据，但可以观察原缓冲区的变化
     */
    @Test
    public void testBuffer04() {
        // 分配10字节的ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        
        // 填充缓冲区：0,1,2,3,4,5,6,7,8,9
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }

        // 创建原缓冲区的只读视图
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        // 修改原缓冲区的数据（每个元素乘以10）
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.get(i);
            b *= 10;
            buffer.put(i, b);
        }

        // 设置只读缓冲区的读取位置和限制
        readOnlyBuffer.position(0);
        readOnlyBuffer.limit(buffer.capacity());

        // 通过只读缓冲区读取数据（此时读取的是修改后的数据）
        while (readOnlyBuffer.remaining() > 0) {
            log.info("{}", readOnlyBuffer.get());
        }
    }

    /**
     * 测试使用Buffer进行文件复制
     * 演示FileChannel配合ByteBuffer实现高效的文件传输
     * 这是NIO相比传统IO更高效的方式
     */
    @Test
    public void testBuffer05() {
        // 获取项目根目录路径
        String projectRoot = System.getProperty("user.dir");
        // 构建资源文件基础路径
        String fileBasePath = projectRoot + "/src/main/resources/";
        // 源文件路径
        String inFile = fileBasePath + "file1.text";
        // 目标文件路径
        String outFile = fileBasePath + "file5.text";
        
        try (
                // 创建输入文件流
                FileInputStream fin = new FileInputStream(inFile);
                // 创建输出文件流
                FileOutputStream fout = new FileOutputStream(outFile);
        ) {
            // 获取输入和输出文件通道
            FileChannel finChannel = fin.getChannel();
            FileChannel foutChannel = fout.getChannel();
            // 分配1024字节的缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            
            // 循环读取和写入数据
            while (true) {
                // 清空缓冲区准备读取
                buffer.clear();
                // 从输入通道读取数据到缓冲区
                int read = finChannel.read(buffer);
                // 如果读取到文件末尾则退出循环
                if (read == -1) {
                    break;
                }
                // 切换缓冲区为写入模式
                buffer.flip();
                // 将缓冲区数据写入输出通道
                foutChannel.write(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException("文件复制过程中发生错误", e);
        }
    }

    /**
     * 测试MappedByteBuffer内存映射文件
     * 演示如何将文件直接映射到内存中进行高效访问
     * 支持随机访问文件的任意位置
     */
    @Test
    public void testBuffer06() {
        // 映射起始位置
        int start = 0;
        // 映射大小（1024字节）
        int size = 1024;
        
        try (RandomAccessFile raf = new RandomAccessFile(BufferDemo.class.getResource("/file1.text").toURI().getPath(), "rw")) {
            // 获取文件通道
            FileChannel fc = raf.getChannel();
            // 创建内存映射缓冲区，支持读写模式
            MappedByteBuffer map = fc.map(FileChannel.MapMode.READ_WRITE, start, size);
            
            // 在映射区域的第0个字节位置写入字符'a'(ASCII值97)
            map.put(0, (byte) 97);
            // 在映射区域的第1023个字节位置写入字符'z'(ASCII值122)
            map.put(1023, (byte) 122);

            ByteBuffer readOnlyBuffer = map.asReadOnlyBuffer();
            CharBuffer decode = StandardCharsets.UTF_8.decode(readOnlyBuffer);
            log.info("{}", decode);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("内存映射文件操作失败", e);
        }
    }
}
