package com.coderlee.nio.asyncfilechannel;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 异步文件通道演示类
 * 展示Java NIO中AsynchronousFileChannel的使用方法
 * 包含异步读写文件的两种方式：Future模式和CompletionHandler模式
 *
 * @author coderlee
 * @since 2026-02-25
 */
@Slf4j
public class AsyncFileChannelDemo {
    
    /**
     * 使用Future模式异步读取文件
     * 通过Future对象轮询检查读取状态，适合简单的异步场景
     * 
     * 读取流程：
     * 1. 打开异步文件通道
     * 2. 分配与文件大小相同的ByteBuffer
     * 3. 发起异步读取操作
     * 4. 轮询Future状态直到完成
     * 5. 处理读取到的数据
     */
    @Test
    public void testReadAsyncFileChannelFuture() {
        // 构建文件路径
        Path file = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "file1.text");
        
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(
                file,
                StandardOpenOption.READ
        )) {
            // 根据文件大小分配缓冲区，确保能一次性读完
            ByteBuffer buffer = ByteBuffer.allocate((int) Files.size(file));
            
            // 发起异步读取操作，从文件开始位置(0)读取
            Future<Integer> future = asynchronousFileChannel.read(buffer, 0);
            
            // 轮询检查读取是否完成
            while (!future.isDone()) {
                log.info("waiting...");
                TimeUnit.SECONDS.sleep(1);
            }
            
            // 准备读取缓冲区中的数据
            buffer.flip();
            
            // 读取并输出所有数据
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            log.info("{}", new String(data));
            
            // 清空缓冲区
            buffer.clear();
            
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 使用CompletionHandler模式异步读取文件
     * 通过回调函数处理读取结果，更加优雅的异步编程方式
     * 
     * 特点：
     * - 非阻塞式异步操作
     * - 通过回调处理成功和失败情况
     * - 需要同步机制等待异步操作完成
     */
    @Test
    public void testReadAsyncFileChannelCompletionHandler() {
        // 文件路径
        Path file = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "file1.text");
        
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(
                file,
                StandardOpenOption.READ
        )) {
            // 分配与文件大小相等的缓冲区
            ByteBuffer buffer = ByteBuffer.allocate((int) Files.size(file));
            
            // 发起异步读取，使用CompletionHandler处理结果
            asynchronousFileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    log.info("result: {}", result);
                    
                    // 准备读取数据
                    attachment.flip();
                    byte[] data = new byte[attachment.limit()];
                    attachment.get(data);
                    
                    // 输出读取到的内容
                    log.info("read content: {}", new String(data));
                    attachment.clear();
                }
                
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    // 处理读取失败的情况
                    log.error("error", exc);
                }
            });
            
            // 等待异步操作完成（简单粗暴的方式）
            TimeUnit.SECONDS.sleep(3);
            
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 使用Future模式异步写入文件
     * 将文本内容异步写入到指定文件中
     * 
     * 写入流程：
     * 1. 打开异步文件写入通道
     * 2. 准备要写入的文本内容
     * 3. 将内容放入ByteBuffer
     * 4. 发起异步写入操作
     * 5. 等待写入完成
     */
    @Test
    public void testWriteAsyncFileChannelFuture() {
        // 目标文件路径
        Path file = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "file7.text");
        
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                file,
                StandardOpenOption.WRITE
        )) {
            // 要写入的文本内容
            String content = "夜里两点，整座城像被谁按了静音键。\n" +
                    "老李的便利店还亮着灯。灯管有点老了，嗡嗡地响，像一只不肯睡觉的蚊子。他正在数零钱，忽然风铃响了一下。\n" +
                    "进来的是个穿雨衣的年轻人，雨衣很干净，却不停往下滴水。\n" +
                    "“要点什么？”老李头也没抬。\n" +
                    "“一个明天。”年轻人说。\n" +
                    "老李愣了一下，抬头看他。那人脸色很白，像很久没见过太阳。\n" +
                    "“卖完了。”老李慢慢说，“昨天就断货了。”\n" +
                    "年轻人叹了口气，把一枚生锈的硬币放在柜台上：“那给我一个昨天也行。”\n" +
                    "老李盯着那枚硬币看了很久，最后从柜台底下拿出一个纸袋，里面是一杯已经凉掉的豆浆和一张旧报纸。\n" +
                    "“只能这些，”他说，“昨天剩下的。”\n" +
                    "年轻人接过来，笑了笑，笑得像要哭。他推门出去的时候，雨忽然停了，街道干干净净，仿佛什么都没发生过。\n" +
                    "第二天清晨，便利店照常营业。\n" +
                    "只是柜台上多了一枚生锈的硬币，怎么收，都收不进钱盒里。";
            
            // 创建缓冲区并放入数据
            ByteBuffer buffer = ByteBuffer.allocate(content.getBytes().length);
            buffer.put(content.getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            
            // 发起异步写入操作
            Future<Integer> future = fileChannel.write(buffer, 0);
            
            // 等待写入完成
            while (!future.isDone());
            
            // 写入完成提示
            log.info("write complete");
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 使用CompletionHandler模式异步写入文件
     * 通过回调函数处理写入结果
     * 
     * 优势：
     * - 写入完成后自动触发回调
     * - 可以在回调中处理写入结果或错误
     * - 更加灵活的异步处理方式
     */
    @Test
    public void testWriteAsyncFileChannelCompletionHandler() {
        // 目标文件路径
        Path file = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "file8.text");
        
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                file,
                StandardOpenOption.WRITE
        )) {
            // 要写入的文本内容
            String content = "夜里两点，整座城像被谁按了静音键。\n" +
                    "老李的便利店还亮着灯。灯管有点老了，嗡嗡地响，像一只不肯睡觉的蚊子。他正在数零钱，忽然风铃响了一下。\n" +
                    "进来的是个穿雨衣的年轻人，雨衣很干净，却不停往下滴水。\n" +
                    "“要点什么？”老李头也没抬。\n" +
                    "“一个明天。”年轻人说。\n" +
                    "老李愣了一下，抬头看他。那人脸色很白，像很久没见过太阳。\n" +
                    "“卖完了。”老李慢慢说，“昨天就断货了。”\n" +
                    "年轻人叹了口气，把一枚生锈的硬币放在柜台上：“那给我一个昨天也行。”\n" +
                    "老李盯着那枚硬币看了很久，最后从柜台底下拿出一个纸袋，里面是一杯已经凉掉的豆浆和一张旧报纸。\n" +
                    "“只能这些，”他说，“昨天剩下的。”\n" +
                    "年轻人接过来，笑了笑，笑得像要哭。他推门出去的时候，雨忽然停了，街道干干净净，仿佛什么都没发生过。\n" +
                    "第二天清晨，便利店照常营业。\n" +
                    "只是柜台上多了一枚生锈的硬币，怎么收，都收不进钱盒里。";
            
            // 创建缓冲区并放入数据
            ByteBuffer buffer = ByteBuffer.allocate(content.getBytes().length);
            buffer.put(content.getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            
            // 发起异步写入，通过CompletionHandler处理结果
            fileChannel.write(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    // 写入成功回调
                    log.info("write complete, result: {}", result);
                }
                
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    // 写入失败回调
                    log.error("error", exc);
                }
            });
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
