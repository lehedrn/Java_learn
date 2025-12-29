package com.coderlee.jdk8features.base64;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class Base64Demo {

    @Test
    public void testBase64() {
        String text = "Hello, Java 8 Base64 API! 这是中文测试。";

        // 标准 Base64 编码
        String standardEncoded = Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
        log.info("Standard Encoded: {}", standardEncoded);

        byte[] standardDecodedBytes = Base64.getDecoder().decode(standardEncoded);
        String standardDecoded = new String(standardDecodedBytes, StandardCharsets.UTF_8);
        log.info("Standard Decoded: {}", standardDecoded);

        // URL 安全 Base64 编码
        String urlEncoded = Base64.getUrlEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
        log.info("URL Encoded: {}", urlEncoded);

        byte[] urlDecodedBytes = Base64.getUrlDecoder().decode(urlEncoded);
        String urlDecoded = new String(urlDecodedBytes, StandardCharsets.UTF_8);
        log.info("URL Decoded: {}", urlDecoded);

        // MIME Base64 编码（适合邮件，自动换行）
        String longText = repeatString(text, 10);
        String mimeEncoded = Base64.getMimeEncoder().encodeToString(longText.getBytes(StandardCharsets.UTF_8));
        log.info("MIME Encoded:\n{}", mimeEncoded);

        byte[] mimeDecodedBytes = Base64.getMimeDecoder().decode(mimeEncoded);
        String mimeDecoded = new String(mimeDecodedBytes, StandardCharsets.UTF_8);
        log.info("MIME Decoded: {}", mimeDecoded);
    }

    public String repeatString(String str, int times) {
        StringBuilder sb = new StringBuilder(str.length() * times);
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

}
