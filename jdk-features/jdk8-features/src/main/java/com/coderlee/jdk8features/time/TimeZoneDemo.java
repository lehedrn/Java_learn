package com.coderlee.jdk8features.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.*;
import java.util.Set;

@Slf4j
public class TimeZoneDemo {

    // 示例 1: 获取和指定时区
    @Test
    public void testZoneId() {
        // 获取系统默认时区
        ZoneId systemZone = ZoneId.systemDefault();
        log.info("System Default Zone: {}", systemZone);

        // 获取指定时区
        ZoneId zoneShanghai = ZoneId.of("Asia/Shanghai");
        log.info("Shanghai Zone: {}", zoneShanghai);

        // 获取所有时区列表
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        log.info("Available Time Zones: {}", availableZoneIds);
    }

    // 示例 2: 创建 ZoneOffset（固定偏移）
    @Test
    public void testZoneOffset() {
        // 创建一个固定偏移，表示 UTC+08:00
        ZoneOffset zoneOffset = ZoneOffset.of("+08:00");
        log.info("ZoneOffset (UTC+8): {}", zoneOffset);

        // 创建一个 UTC-05:00 的固定偏移
        ZoneOffset zoneOffsetMinus = ZoneOffset.of("-05:00");
        log.info("ZoneOffset (UTC-5): {}", zoneOffsetMinus);
    }

    // 示例 3: 创建带时区的日期时间 ZonedDateTime
    @Test
    public void testZonedDateTime() {
        // 使用指定时区创建一个 ZonedDateTime 实例
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2025, 12, 26, 14, 30, 0, 0, ZoneId.of("Asia/Shanghai"));
        log.info("ZonedDateTime in Shanghai: {}", zonedDateTime);

        // 获取当前时区的日期时间
        ZonedDateTime nowInShanghai = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        log.info("Current Time in Shanghai: {}", nowInShanghai);

        // 获取当前 UTC 时间
        ZonedDateTime utcTime = ZonedDateTime.now(ZoneOffset.UTC);
        log.info("Current UTC Time: {}", utcTime);
    }

    // 示例 4: 创建带偏移的日期时间 OffsetDateTime
    @Test
    public void testOffsetDateTime() {
        // 创建一个带偏移的日期时间
        OffsetDateTime offsetDateTime = OffsetDateTime.of(2025, 12, 26, 14, 30, 0, 0, ZoneOffset.of("+08:00"));
        log.info("OffsetDateTime (UTC+8): {}", offsetDateTime);

        // 使用固定偏移量（如 UTC-05:00）创建 OffsetDateTime
        OffsetDateTime offsetDateTimeMinus = OffsetDateTime.of(2025, 12, 26, 14, 30, 0, 0, ZoneOffset.of("-05:00"));
        log.info("OffsetDateTime (UTC-5): {}", offsetDateTimeMinus);
    }

    // 示例 5: 时区转换 - 使用 withZoneSameInstant() 方法
    @Test
    public void testTimeZoneConversion() {
        // 创建上海时间
        ZonedDateTime shanghaiTime = ZonedDateTime.of(2025, 12, 26, 14, 30, 0, 0, ZoneId.of("Asia/Shanghai"));
        log.info("Meeting time in Shanghai: {}", shanghaiTime);

        // 转换到纽约时间
        ZonedDateTime newYorkTime = shanghaiTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        log.info("Meeting time in New York: {}", newYorkTime);
    }

    // 示例 6: 处理夏令时（DST）边界情况
    @Test
    public void testDST() {
        // 夏令时开始时的时间
        ZonedDateTime summerTime = ZonedDateTime.of(2025, 3, 26, 2, 0, 0, 0, ZoneId.of("America/New_York"));
        log.info("Summer Time (DST start): {}", summerTime);

        // 转换到夏令时结束后的时间（加1小时）
        ZonedDateTime standardTime = summerTime.plusHours(1);  // 夏令时结束后
        log.info("Standard Time after DST ends: {}", standardTime);
    }

    // 示例 7: ZonedDateTime 与 Instant 的互转
    @Test
    public void testZonedDateTimeToInstant() {
        // 创建一个 ZonedDateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        log.info("ZonedDateTime in Shanghai: {}", zonedDateTime);

        // 转换为 Instant
        Instant instant = zonedDateTime.toInstant();
        log.info("Instant from ZonedDateTime: {}", instant);

        // 将 Instant 转换回 ZonedDateTime
        ZonedDateTime fromInstant = instant.atZone(ZoneId.of("Asia/Shanghai"));
        log.info("ZonedDateTime from Instant: {}", fromInstant);
    }

    // 示例 8: 跨时区计算会议时间
    @Test
    public void testCrossTimeZoneMeeting() {
        // 创建上海时间的会议时间
        ZonedDateTime shanghaiMeetingTime = ZonedDateTime.of(2025, 12, 26, 14, 30, 0, 0, ZoneId.of("Asia/Shanghai"));
        log.info("Meeting Time in Shanghai: {}", shanghaiMeetingTime);

        // 将会议时间转换为纽约时间
        ZonedDateTime newYorkMeetingTime = shanghaiMeetingTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        log.info("Meeting Time in New York: {}", newYorkMeetingTime);
    }

}
