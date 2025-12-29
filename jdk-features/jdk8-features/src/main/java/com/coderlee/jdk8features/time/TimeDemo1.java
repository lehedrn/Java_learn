package com.coderlee.jdk8features.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TimeDemo1 {
    // 示例 3: LocalDate 使用示例
    @Test
    public void testLocalDate() {
        LocalDate today = LocalDate.now();
        log.info("Today's Date: {}", today);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(formatter);
        log.info("Formatted Date: {}", formattedDate);
    }

    // 示例 4: LocalTime 使用示例
    @Test
    public void testLocalTime() {
        LocalTime time = LocalTime.now();
        log.info("Current Time: {}", time);

        LocalTime specificTime = LocalTime.of(14, 30);
        log.info("Specific Time: {}", specificTime);
    }

    // 示例 5: LocalDateTime 使用示例
    @Test
    public void testLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Current DateTime: {}", now);

        LocalDateTime futureDateTime = now.plusDays(5);
        log.info("Future DateTime (5 days later): {}", futureDateTime);
    }

    // 示例 6: ZonedDateTime 使用示例
    @Test
    public void testZonedDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        log.info("Current ZonedDateTime: {}", zonedDateTime);

        ZonedDateTime specificZonedDateTime = ZonedDateTime.of(2025, 12, 26, 14, 30, 0, 0, ZoneId.of("America/New_York"));
        log.info("Specific ZonedDateTime: {}", specificZonedDateTime);
    }

    // 示例 7: Instant 使用示例
    @Test
    public void testInstant() {
        Instant instant = Instant.now();
        log.info("Current Instant: {}", instant);

        Instant specificInstant = Instant.parse("2025-12-26T14:30:00Z");
        log.info("Specific Instant: {}", specificInstant);
    }

    // 示例 8: Duration 使用示例
    @Test
    public void testDuration() {
        LocalTime start = LocalTime.of(14, 30);
        LocalTime end = LocalTime.of(16, 45);
        Duration duration = Duration.between(start, end);
        log.info("Duration between times: {}", duration);

        long minutes = duration.toMinutes();
        log.info("Duration in minutes: {}", minutes);
    }

    // 示例 9: Period 使用示例
    @Test
    public void testPeriod() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 12, 31);
        Period period = Period.between(start, end);
        log.info("Period between dates: {} years, {} months, {} days", period.getYears(), period.getMonths(), period.getDays());
    }
}
