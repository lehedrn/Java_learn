package com.coderlee.jdk8features.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

@Slf4j
public class TimeCalculationDemo {

    // 示例 1: Duration 示例 - 时间量（机器时间）
    @Test
    public void testDuration() {
        // 创建Duration对象：2小时
        Duration duration1 = Duration.ofHours(2);
        log.info("Duration of 2 hours: {}", duration1);

        // 创建Duration对象：30分钟
        Duration duration2 = Duration.ofMinutes(30);
        log.info("Duration of 30 minutes: {}", duration2);

        // 计算两个时间点之间的Duration（如：NOON 到 MIDNIGHT）
        Duration duration3 = Duration.between(LocalTime.NOON, LocalTime.MIDNIGHT);
        log.info("Duration between NOON and MIDNIGHT: {}", duration3);

        // Duration加法
        Duration totalDuration = duration1.plus(duration2);  // 2小时 + 30分钟
        log.info("Total Duration (2 hours + 30 minutes): {}", totalDuration);

        // Duration减法
        totalDuration = totalDuration.minusMinutes(45);  // 减去45分钟
        log.info("Updated Duration after subtracting 45 minutes: {}", totalDuration);
    }

    // 示例 2: Period 示例 - 日期量（人类时间）
    @Test
    public void testPeriod() {
        // 创建Period对象：1年
        Period period1 = Period.ofYears(1);
        log.info("Period of 1 year: {}", period1);

        // 创建Period对象：6个月
        Period period2 = Period.ofMonths(6);
        log.info("Period of 6 months: {}", period2);

        // 计算两个日期之间的Period（如：从2020年1月1日到今天的差距）
        Period period3 = Period.between(LocalDate.of(2020, 1, 1), LocalDate.now());
        log.info("Period between 2020-01-01 and today: {}", period3);

        // Period加法
        period3 = period3.plusMonths(2);  // 增加2个月
        log.info("Period after adding 2 months: {}", period3);

        // Period减法
        period3 = period3.minusYears(1);  // 减去1年
        log.info("Period after subtracting 1 year: {}", period3);
    }

    // 示例 3: ChronoUnit 示例 - 时间单位计算
    @Test
    public void testChronoUnit() {
        // 计算两个日期之间的天数
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.of(2025, 1, 1), LocalDate.now());
        log.info("Days between 2025-01-01 and today: {}", daysBetween);

        // 计算两个日期之间的月数
        long monthsBetween = ChronoUnit.MONTHS.between(LocalDate.of(2025, 1, 1), LocalDate.now());
        log.info("Months between 2025-01-01 and today: {}", monthsBetween);

        // 计算两个日期之间的年数
        long yearsBetween = ChronoUnit.YEARS.between(LocalDate.of(2025, 1, 1), LocalDate.now());
        log.info("Years between 2025-01-01 and today: {}", yearsBetween);

        // 在某个日期上加5天
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plus(5, ChronoUnit.DAYS);
        log.info("Future Date after 5 days: {}", futureDate);
    }

    // 示例 4: Temporal 接口 - 通用时间操作
    @Test
    public void testTemporal() {
        LocalDateTime now = LocalDateTime.now();

        // 使用 Temporal.plus() 添加5天
        LocalDateTime futureDateTime = now.plus(5, ChronoUnit.DAYS);
        log.info("Future DateTime after 5 days: {}", futureDateTime);

        // 使用 Temporal.until() 计算与未来时间的差距
        long daysUntil = now.until(futureDateTime, ChronoUnit.DAYS);
        log.info("Days until future date: {}", daysUntil);
    }

    // 示例 5: TemporalAmount - 加法与减法操作
    @Test
    public void testTemporalAmount() {
        // 使用 TemporalAmount 来增加和减少时间
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        TemporalAmount addAmount = Duration.ofDays(10);  // 10天
        TemporalAmount subtractAmount = Period.ofMonths(2);  // 2个月

        // 增加10天
        LocalDate futureDate = startDate.plus(addAmount);
        log.info("Date after adding 10 days: {}", futureDate);

        // 减去2个月
        futureDate = futureDate.minus(subtractAmount);
        log.info("Date after subtracting 2 months: {}", futureDate);
    }

    // 示例 6: 计算年龄
    @Test
    public void testAgeCalculation() {
        // 计算年龄
        LocalDate birthDate = LocalDate.of(1990, 5, 15);
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthDate, currentDate);
        log.info("Age: {} years, {} months, {} days", age.getYears(), age.getMonths(), age.getDays());
    }

    // 示例 7: 跳过周末计算工作日
    @Test
    public void testWorkdaysCalculation() {
        // 计算从今天起10个工作日后的日期，跳过周末
        LocalDate startDate = LocalDate.now();
        LocalDate resultDate = startDate;
        int workDaysCount = 0;
        while (workDaysCount < 10) {
            resultDate = resultDate.plusDays(1);
            if (resultDate.getDayOfWeek() != DayOfWeek.SATURDAY && resultDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workDaysCount++;
            }
        }
        log.info("Result date after 10 workdays: {}", resultDate);
    }

    // 示例 8: 计算两个日期之间的天数
    @Test
    public void testDaysBetween() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(start, end);
        log.info("Days between start and today: {}", daysBetween);
    }

    // 示例 9: 持续时间相加
    @Test
    public void testDurationAddition() {
        Duration duration1 = Duration.ofHours(2);
        Duration duration2 = Duration.ofMinutes(30);
        Duration totalDuration = duration1.plus(duration2);  // 相加
        log.info("Total Duration (2 hours + 30 minutes): {}", totalDuration);
    }

    // 示例 10: 周期应用到订阅服务
    @Test
    public void testSubscriptionPeriod() {
        LocalDate subscriptionStartDate = LocalDate.of(2025, 1, 1);
        Period subscriptionPeriod = Period.ofMonths(3); // 订阅周期 3 个月
        LocalDate nextPaymentDate = subscriptionStartDate.plus(subscriptionPeriod);
        log.info("Next Payment Date: {}", nextPaymentDate);
    }
}
