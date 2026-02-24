package com.coderlee.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件通道演示类2 - 演示如何使用FileChannel向文件写入数据
 * 
 * 该类展示了使用NIO的FileChannel API将字符串数据写入文件的基本操作流程，
 * 包括缓冲区的分配、数据写入以及资源管理等内容
 */
@Slf4j
public class FileChannelDemo2 {

    /**
     * 主方法 - 执行文件写入操作
     * 
     * 该方法执行以下步骤：
     * 1. 打开一个随机访问文件并获取其通道
     * 2. 创建一个字节缓冲区
     * 3. 准备要写入的数据
     * 4. 将数据写入缓冲区并翻转
     * 5. 通过通道将数据写入文件
     * 6. 关闭通道和文件
     * 
     * @param args 命令行参数
     * @throws URISyntaxException 如果文件路径格式错误
     */
    public static void main(String[] args) throws URISyntaxException {
        log.info("开始写入");
        // 使用try-with-resources语句确保资源正确关闭
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(
                System.getProperty("user.dir") + "/java-nio/nio-learn/src/main/resources/file2.text", "rw")) {
//          try (RandomAccessFile randomAccessFile = new RandomAccessFile(
//                "src/main/resources/file2.text", "rw")) {
            // 获取文件通道，用于读写文件
            FileChannel channel = randomAccessFile.getChannel();
            
            // 分配容量为4096字节的ByteBuffer缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            
            // 要写入文件的数据 - 一段富有诗意的小故事
            String newData = "夜里两点，整座城像被谁按了静音键。\r\n" + 
                    "老李的便利店还亮着灯。灯管有点老了，嗡嗡地响，像一只不肯睡觉的蚊子。他正在数零钱，忽然风铃响了一下。\r\n" + 
                    "进来的是个穿雨衣的年轻人，雨衣很干净，却不停往下滴水。\r\n" + 
                    "“要点什么？”老李头也没抬。\r\n" + 
                    "“一个明天。”年轻人说。\r\n" + 
                    "老李愣了一下，抬头看他。那人脸色很白，像很久没见过太阳。\r\n" + 
                    "“卖完了。”老李慢慢说，“昨天就断货了。”\r\n" + 
                    "年轻人叹了口气，把一枚生锈的硬币放在柜台上：“那给我一个昨天也行。”\r\n" + 
                    "老李盯着那枚硬币看了很久，最后从柜台底下拿出一个纸袋，里面是一杯已经凉掉的豆浆和一张旧报纸。\r\n" + 
                    "“只能这些，”他说，“昨天剩下的。”\r\n" + 
                    "年轻人接过来，笑了笑，笑得像要哭。他推门出去的时候，雨忽然停了，街道干干净净，仿佛什么都没发生过。\r\n" + 
                    "第二天清晨，便利店照常营业。\r\n" + 
                    "只是柜台上多了一枚生锈的硬币，怎么收，都收不进钱盒里。";
            
            // 记录待写入数据的字节数长度
            log.info("data bytes length: {}", newData.getBytes().length);
            
            // 清空缓冲区，准备写入新数据
            buffer.clear();
            
            // 将字符串数据按照UTF-8编码转换为字节并放入缓冲区
            buffer.put(newData.getBytes(StandardCharsets.UTF_8));
            
            // 翻转缓冲区，准备从缓冲区读取数据写入文件
            // limit设置为当前position，position重置为0
            buffer.flip();
            
            // 循环将缓冲区中的数据写入文件通道
            // 直到缓冲区中没有剩余数据为止
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            
            // 关闭通道（实际会由try-with-resources自动处理）
            // channel.close();
            
            log.info("写入完成");
        } catch (IOException e) {
            // 记录IO异常信息
            log.error("error", e);
        }
    }
}