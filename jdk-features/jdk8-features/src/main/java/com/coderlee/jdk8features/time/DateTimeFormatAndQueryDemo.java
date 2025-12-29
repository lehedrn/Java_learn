package com.coderlee.jdk8features.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.*;
import java.util.Locale;

@Slf4j
public class DateTimeFormatAndQueryDemo {

    /**
     * ================================
     * 1. DateTimeFormatter 预定义格式
     * ================================
     */
    @Test
    public void testPredefinedFormatter() {
        LocalDate today = LocalDate.now();

        String isoDate = today.format(DateTimeFormatter.ISO_DATE);
        String isoLocalDate = today.format(DateTimeFormatter.ISO_LOCAL_DATE);

        log.info("ISO_DATE       : {}", isoDate);
        log.info("ISO_LOCAL_DATE : {}", isoLocalDate);
    }

    /**
     * ================================
     * 2. 自定义格式化（ofPattern）
     * ================================
     */
    @Test
    public void testCustomFormatter() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String formatted = now.format(formatter);

        log.info("Custom formatted datetime: {}", formatted);
    }

    /**
     * ================================
     * 3. 字符串解析为日期时间对象
     * ================================
     */
    @Test
    public void testParseDateTime() {
        String dateStr = "2025-12-26";
        String dateTimeStr = "2025-12-26 14:30:00";

        LocalDate date =
                LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);

        LocalDateTime dateTime =
                LocalDateTime.parse(
                        dateTimeStr,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                );

        log.info("Parsed LocalDate     : {}", date);
        log.info("Parsed LocalDateTime : {}", dateTime);
    }

    /**
     * ================================
     * 4. 解析异常处理（DateTimeParseException）
     * ================================
     */
    @Test
    public void testParseException() {
        String invalidDate = "2025-12-32";

        try {
            LocalDate.parse(invalidDate, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            log.error("Date parse failed: {}", invalidDate, e);
        }
    }

    /**
     * ================================
     * 5. 日志时间解析（真实业务场景）
     * ================================
     */
    @Test
    public void testLogTimeParsing() {
        String logTime = "2025-12-26 18:45:10";

        DateTimeFormatter logFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime logDateTime =
                LocalDateTime.parse(logTime, logFormatter);

        log.info("Parsed log time: {}", logDateTime);
    }

    /**
     * ================================
     * 6. TemporalAdjuster：调整到月末
     * ================================
     */
    @Test
    public void testAdjustToEndOfMonth() {
        LocalDate today = LocalDate.now();

        LocalDate endOfMonth =
                today.with(TemporalAdjusters.lastDayOfMonth());

        log.info("Today      : {}", today);
        log.info("End of month: {}", endOfMonth);
    }

    /**
     * ================================
     * 7. TemporalAdjuster：下一个工作日
     * ================================
     */
    @Test
    public void testNextWorkDay() {
        LocalDate today = LocalDate.now();

        LocalDate nextMonday =
                today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        log.info("Today        : {}", today);
        log.info("Next Monday  : {}", nextMonday);
    }

    /**
     * ================================
     * 8. TemporalQuery：查询是否为闰年
     * ================================
     */
    @Test
    public void testLeapYearQuery() {
        LocalDate date = LocalDate.of(2024, 2, 29);

        Boolean isLeapYear = date.isLeapYear();

        log.info("Date       : {}", date);
        log.info("Is leap year: {}", isLeapYear);
    }

    /**
     * ================================
     * 9. 自定义 TemporalQuery
     * ================================
     */
    @Test
    public void testCustomTemporalQuery() {
        LocalDateTime now = LocalDateTime.now();

        TemporalQuery<Boolean> isMorningQuery = temporal -> {
            int hour = temporal.get(ChronoField.HOUR_OF_DAY);
            return hour < 12;
        };

        Boolean isMorning = now.query(isMorningQuery);

        log.info("Now        : {}", now);
        log.info("Is morning : {}", isMorning);
    }

    /**
     * ================================
     * 10. 国际化格式（Locale）
     * ================================
     */
    @Test
    public void testLocaleFormatter() {
        LocalDate date = LocalDate.of(2025, 12, 26);

        DateTimeFormatter usFormatter =
                DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.US);

        DateTimeFormatter frFormatter =
                DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRANCE);

        log.info("US format : {}", date.format(usFormatter));
        log.info("FR format : {}", date.format(frFormatter));
    }
}
