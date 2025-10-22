package com.coderlee.juc;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;


/**
 * ForkJoinPoolDemo 类用于演示三种不同的方式计算从 0 到 n 的累加和，并比较它们的性能。
 * 包含以下方法：
 * - handler1: 使用普通 for 循环计算。
 * - handler2: 使用 Java Stream API 的并行流计算。
 * - handler3: 使用 ForkJoinPool 实现分治算法进行并行计算。
 */
public class ForkJoinPoolDemo {

    public static void main(String[] args) {
        long n = 50000000000L; // 定义一个大整数 n 作为计算范围
        handler1(n); // 调用普通 for 循环方法
        handler2(n); // 调用 Stream API 并行流方法
        handler3(n); // 调用 ForkJoinPool 方法
    }

    /**
     * 使用普通 for 循环计算从 0 到 n 的累加和。
     * @param n 计算范围的最大值
     */
    public static void handler1(long n) {
        Instant start = Instant.now(); // 记录开始时间
        long sum = 0L; // 初始化累加和
        for (long i = 0; i <= n; i++) {
            sum += i; // 累加每个数字
        }
        System.out.println("for循环：结果 " + sum + " , 耗时：" 
                + Duration.between(start, Instant.now()).toMillis() + " 毫秒");
    }

    /**
     * 使用 Java Stream API 的并行流计算从 0 到 n 的累加和。
     * @param n 计算范围的最大值
     */
    public static void handler2(long n) {
        Instant start = Instant.now(); // 记录开始时间
        // 使用 LongStream.rangeClosed 创建范围流，并通过 parallel 方法启用并行计算
        long sum = LongStream.rangeClosed(0L, n).parallel().reduce(0L, Long::sum);
        System.out.println("StreamAPI：结果 " + sum + " , 耗时：" 
                + Duration.between(start, Instant.now()).toMillis() + " 毫秒");
    }

    /**
     * 使用 ForkJoinPool 实现分治算法计算从 0 到 n 的累加和。
     * @param n 计算范围的最大值
     */
    public static void handler3(long n) {
        Instant start = Instant.now(); // 记录开始时间
        ForkJoinPool forkJoinPool = new ForkJoinPool(); // 创建 ForkJoinPool 实例
        ForkJoinTask<Long> task = new ForkJoinSumCalculate(0L, n); // 创建任务实例
        Long sum = forkJoinPool.invoke(task); // 提交任务并获取结果
        System.out.println("ForkJoin：结果 " + sum + " , 耗时：" 
                + Duration.between(start, Instant.now()).toMillis() + " 毫秒");
        forkJoinPool.shutdown();
    }
}

/**
 * ForkJoinSumCalculate 是一个 RecursiveTask，用于通过分治算法计算从 start 到 end 的累加和。
 */
class ForkJoinSumCalculate extends RecursiveTask<Long> {
    
    private static final long serialVersionUID = 1L;

    private long start; // 计算范围的起始值
    private long end;   // 计算范围的结束值

    private static final long THRESHOLD = 10000L; // 阈值，当任务规模小于该值时直接计算

    /**
     * 构造函数，初始化计算范围。
     * @param start 起始值
     * @param end 结束值
     */
    public ForkJoinSumCalculate(long start, long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * 重写 compute 方法，实现分治逻辑。
     * 如果任务规模小于阈值，则直接计算；否则将任务拆分为两个子任务并行处理。
     * @return 累加和
     */
    @Override
    protected Long compute() {
        long length = end - start; // 计算当前任务的范围长度
        if (length < THRESHOLD) { // 如果范围小于阈值，直接计算
            long sum = 0L;
            for (long i = start; i <= end; i++) {
                sum += i; // 累加每个数字
            }
            return sum;
        }
        long middle = (start + end) / 2; // 计算中间值，用于拆分任务
        ForkJoinSumCalculate left = new ForkJoinSumCalculate(start, middle); // 左子任务
        left.fork(); // 异步执行左子任务
        ForkJoinSumCalculate right = new ForkJoinSumCalculate(middle + 1, end); // 右子任务
        right.fork(); // 异步执行右子任务
        return left.join() + right.join(); // 合并左右子任务的结果
    } 
}