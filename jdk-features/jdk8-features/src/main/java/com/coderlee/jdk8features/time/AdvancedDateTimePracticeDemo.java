package com.coderlee.jdk8features.time;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
public class AdvancedDateTimePracticeDemo {

    /**
     * ================================
     * 1. JDBC 集成：Timestamp <-> LocalDateTime
     * ================================
     */
    @Test
    public void testJdbcIntegration() {
        // 模拟 JDBC 返回的 Timestamp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // JDBC -> Java 8
        LocalDateTime ldt = timestamp.toLocalDateTime();
        log.info("Timestamp -> LocalDateTime: {}", ldt);

        // Java 8 -> JDBC
        Timestamp newTimestamp = Timestamp.valueOf(ldt);
        log.info("LocalDateTime -> Timestamp: {}", newTimestamp);
    }

    /**
     * ================================
     * 2. Clock：固定时间用于测试
     * ================================
     */
    @Test
    public void testClockForTesting() {
        Clock fixedClock = Clock.fixed(
                Instant.parse("2025-12-26T10:00:00Z"),
                ZoneId.of("UTC")
        );

        LocalDateTime now = LocalDateTime.now(fixedClock);
        LocalDate today = LocalDate.now(fixedClock);

        log.info("Fixed LocalDateTime: {}", now);
        log.info("Fixed LocalDate    : {}", today);
    }

    /**
     * ================================
     * 3. Year / Month / Quarter 计算
     * ================================
     */
    @Test
    public void testYearMonthAndQuarter() {
        LocalDate date = LocalDate.of(2025, Month.DECEMBER, 26);

        Year year = Year.from(date);
        Month month = date.getMonth();

        int quarter = (month.getValue() - 1) / 3 + 1;

        log.info("Year    : {}", year);
        log.info("Month   : {}", month);
        log.info("Quarter : Q{}", quarter);
    }

    /**
     * ================================
     * 4. 迁移示例：Date -> Instant -> LocalDateTime
     * ================================
     */
    @Test
    public void testLegacyDateMigration() {
        // 旧 API
        java.util.Date legacyDate = new java.util.Date();

        // Date -> Instant
        Instant instant = legacyDate.toInstant();

        // Instant -> LocalDateTime（指定时区）
        LocalDateTime ldt =
                LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        log.info("Legacy Date        : {}", legacyDate);
        log.info("Converted Instant  : {}", instant);
        log.info("LocalDateTime      : {}", ldt);
    }

    /**
     * ================================
     * 5. 格式化器缓存（性能最佳实践）
     * ================================
     */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    public void testFormatterCache() {
        LocalDateTime now = LocalDateTime.now();
        String formatted = now.format(FORMATTER);

        log.info("Formatted datetime: {}", formatted);
    }

    /**
     * ================================
     * 6. Locale 国际化格式
     * ================================
     */
    @Test
    public void testLocaleFormatting() {
        LocalDate date = LocalDate.of(2025, 12, 26);

        DateTimeFormatter usFormatter =
                DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.US);

        DateTimeFormatter cnFormatter =
                DateTimeFormatter.ofPattern("yyyy年MM月dd日", Locale.CHINA);

        log.info("US format : {}", date.format(usFormatter));
        log.info("CN format : {}", date.format(cnFormatter));
    }

    /**
     * ================================
     * 7. 时区变化 & ZoneId 使用
     * ================================
     */
    @Test
    public void testZoneIdUsage() {
        LocalDateTime localTime = LocalDateTime.of(2025, 12, 26, 20, 0);

        ZonedDateTime shanghaiTime =
                localTime.atZone(ZoneId.of("Asia/Shanghai"));

        ZonedDateTime newYorkTime =
                shanghaiTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        log.info("Shanghai time : {}", shanghaiTime);
        log.info("New York time : {}", newYorkTime);
    }

    /**
     * ================================
     * 8. 实战案例：预约系统（创建）
     * ================================
     */
    @Test
    public void testAppointmentCreation() {
        LocalDateTime userLocalTime =
                LocalDateTime.of(2025, 12, 26, 14, 0);

        ZoneId userZone = ZoneId.of("Asia/Shanghai");

        Appointment appointment =
                Appointment.create(userLocalTime, userZone);

        log.info("Appointment stored instant : {}", appointment.getStartTime());
        log.info("User zone                  : {}", appointment.getUserZone());
    }

    /**
     * ================================
     * 9. 实战案例：预约时间展示
     * ================================
     */
    @Test
    public void testAppointmentDisplay() {
        Appointment appointment = new Appointment();
        appointment.setStartTime(
                Instant.parse("2025-12-26T06:00:00Z"));
        appointment.setUserZone(ZoneId.of("Asia/Shanghai"));

        String display =
                appointment.display(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        log.info("Displayed appointment time: {}", display);
    }

    /**
     * ================================
     * 预约系统领域模型
     */
    @Data
    static class Appointment {
        /**
         * 统一存储为 UTC 时间
         */
        private Instant startTime;

        /**
         * 用户时区
         */
        private ZoneId userZone;

        /**
         * 创建预约
         */
        public static Appointment create(
                LocalDateTime localTime,
                ZoneId userZone) {

            Instant instant =
                    localTime.atZone(userZone).toInstant();

            Appointment appt = new Appointment();
            appt.setStartTime(instant);
            appt.setUserZone(userZone);
            return appt;
        }

        /**
         * 按用户时区展示
         */
        public String display(DateTimeFormatter formatter) {
            ZonedDateTime userTime =
                    startTime.atZone(userZone);
            return userTime.format(formatter);
        }
    }
}
