package com.coderlee.nio.charset;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

@Slf4j
public class CharsetDemo {
    
    /**
     * 主方法 - 演示字符集编码解码的各种操作
     * 
     * 执行流程：
     * 1. 创建UTF-8编码器
     * 2. 准备测试文本内容
     * 3. 将字符串编码为字节序列
     * 4. 解码字节序列为原始字符串
     * 5. 演示跨字符集解码（UTF-8字节用GBK解码）
     * 6. 列出系统支持的所有字符集
     * 
     * @param args 命令行参数（未使用）
     * @throws CharacterCodingException 字符编码异常
     */
    public static void main(String[] args) throws CharacterCodingException {
        // 获取UTF-8字符集对象
        Charset utf8_charset = StandardCharsets.UTF_8;
        
        // 创建UTF-8编码器，用于将字符转换为字节
        CharsetEncoder utf8Encoder = utf8_charset.newEncoder();
        
        // 测试文本内容（包含中文字符）
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
        
        // 创建字符缓冲区，容量为UTF-8编码后的字节长度
        CharBuffer charBuffer = CharBuffer.allocate(content.getBytes(utf8_charset).length);
        
        // 将文本内容放入字符缓冲区
        charBuffer.put(content);
        
        // 翻转缓冲区，准备进行编码操作
        charBuffer.flip();
        
        // ==================== UTF-8 编码演示 ====================
        // 使用UTF-8编码器将字符缓冲区编码为字节缓冲区
        ByteBuffer utf8Buffer = utf8Encoder.encode(charBuffer);
        
        // 输出编码后的字节序列
        log.info("使用utf8编码后的结果:");
        for (int i = 0; i < utf8Buffer.limit(); i++) {
            // 逐字节输出编码结果
            log.info("{}", utf8Buffer.get());
        }
        
        // ==================== UTF-8 解码演示 ====================
        // 重置缓冲区位置，准备解码
        utf8Buffer.flip();
        
        // 创建UTF-8解码器，用于将字节转换回字符
        CharsetDecoder utf8Decoder = utf8_charset.newDecoder();
        
        // 使用UTF-8解码器解码字节缓冲区
        CharBuffer utf8DecoderBuffer = utf8Decoder.decode(utf8Buffer);
        
        // 输出解码后的字符串结果
        log.info("使用utf8解码后的结果: {}", utf8DecoderBuffer.toString());
        
        // ==================== 跨字符集解码演示 ====================
        // 获取GBK字符集对象
        Charset gbk_charset = Charset.forName("GBK");
        
        // 重新定位UTF-8字节缓冲区到开始位置
        utf8Buffer.flip();
        
        // 使用GBK解码器解码原本是UTF-8编码的字节（会产生乱码）
        CharBuffer gbkBuffer = gbk_charset.decode(utf8Buffer);
        
        // 输出使用错误字符集解码的结果（演示编码不匹配的问题）
        log.info("使用GBK解码后的结果: {}", gbkBuffer.toString());
        
        // ==================== 系统字符集查询 ====================
        // 获取JVM支持的所有字符集，按键名排序
        SortedMap<String, Charset> map = Charset.availableCharsets();
        
        // 获取字符集映射的条目集合
        Set<Map.Entry<String, Charset>> set = map.entrySet();
        
        // 遍历并输出所有支持的字符集
        for (Map.Entry<String, Charset> entry : set) {
            log.info("{} = {}", entry.getKey(), entry.getValue());
        }
    }
}
