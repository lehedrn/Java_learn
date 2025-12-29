package com.coderlee.jdk8features.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class DateFormatDemo {

    @Test
    public void testUnSafe() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateFormate(10, () -> sdf.parse("2025-12-26"));
    }

    @Test
    public void testSafe() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateFormate(10, () -> LocalDate.parse("2025-12-26", dtf));
    }

    private void dateFormate(int threadNums, Callable<?> task) {
        ExecutorService pool = Executors.newFixedThreadPool(threadNums);
        List<Future<?>> results = new ArrayList<>();
        try {
            for (int i = 0; i < 30; i++) {
                Future<?> future = pool.submit(task);
                results.add(future);
            }
            for (Future<?> result : results) {
                try {
                    log.info("{}", result.get());
                } catch (Exception e) {
                    log.error("date format error", e);
                }
            }
        } finally {
            pool.shutdown();
        }
    }

}
