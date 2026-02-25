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

@Slf4j
public class AsyncFileChannelDemo {
    @Test
    public void testReadAsyncFileChannelFuture() {
        Path file = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "file1.text");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(
//                Paths.get(System.getProperty("user.dir"), "java-nio", "nio-learn", "src", "main", "resources", "file1.text"),
                // 单元测试下，可以直接到nio-learn工程
                file,
                StandardOpenOption.READ
        )) {
            ByteBuffer buffer = ByteBuffer.allocate((int) Files.size(file));
            Future<Integer> future = asynchronousFileChannel.read(buffer, 0);
            while (!future.isDone()) {
                log.info("waiting...");
                TimeUnit.SECONDS.sleep(1);
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                byte[] data = new byte[buffer.limit()];
                buffer.get(data);
                log.info("{}", new String(data));
            }
            buffer.clear();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testReadAsyncFileChannelCompletionHandler() {
        Path file = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "file1.text");
        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(
                file,
                StandardOpenOption.READ
        )) {
            ByteBuffer buffer = ByteBuffer.allocate((int) Files.size(file));
            asynchronousFileChannel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    log.info("result: {}", result);
                    attachment.flip();
                    byte[] data = new byte[attachment.limit()];
                    attachment.get(data);
                    log.info("read content: {}", new String(data));
                    attachment.clear();
                }
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    log.error("error", exc);
                }
            });

            TimeUnit.SECONDS.sleep(3);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testWriteAsyncFileChannelFuture() {
        Path file = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "file7.text");
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                file,
                StandardOpenOption.WRITE
        )) {
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
            ByteBuffer buffer = ByteBuffer.allocate(content.getBytes().length);
            buffer.put(content.getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            Future<Integer> future = fileChannel.write(buffer, 0);
            while (!future.isDone());
            log.info("write complete");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testWriteAsyncFileChannelCompletionHandler() {
        Path file = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "file8.text");
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                file,
                StandardOpenOption.WRITE
        )) {
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
            ByteBuffer buffer = ByteBuffer.allocate(content.getBytes().length);
            buffer.put(content.getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            fileChannel.write(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    log.info("write complete, result: {}", result);
                }
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    log.error("error", exc);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
