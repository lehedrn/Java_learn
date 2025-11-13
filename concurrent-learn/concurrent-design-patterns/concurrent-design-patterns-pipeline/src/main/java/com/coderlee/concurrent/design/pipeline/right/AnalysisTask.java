package com.coderlee.concurrent.design.pipeline.right;

import com.coderlee.concurrent.design.pipeline.framework.AbstractPipe;
import com.coderlee.concurrent.design.pipeline.framework.Pipe;
import com.coderlee.concurrent.design.pipeline.framework.PipeException;
import com.coderlee.concurrent.design.pipeline.framework.SimplePipeline;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 分析任务执行器
 * <p>
 * 实现了{@link Runnable}接口，负责构建并运行一个并发流水线来处理文本数据分析工作流。
 * </p>
 *
 * @see SimplePipeline
 * @see Pipe
 */
@Slf4j
public class AnalysisTask implements Runnable {

    /**
     * 存储累计分析结果的原子长整型变量
     */
    private volatile AtomicLong analysisResult = new AtomicLong(0);

    /**
     * 文件总行数
     */
    private int fileLineNum;

    /**
     * 构造函数初始化文件行数
     *
     * @param fileLineNum 要处理的文件行数
     */
    public AnalysisTask(int fileLineNum) {
        this.fileLineNum = fileLineNum;
    }

    /**
     * 执行分析任务的方法
     * <p>
     * 创建并启动一个简单的并发流水线，然后读取指定数量的文件行作为输入传递给流水线处理，
     * 最后等待所有任务完成并关闭流水线。
     * </p>
     */
    @Override
    public void run() {
        // 输入String, 输出Long，输出结果为累加值
        SimplePipeline<String, Long> pipeline = this.buildPipeline();
        pipeline.init(pipeline.newDefaultPipelineContext());
        try {
            this.readFile(pipeline);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pipeline.shutdown(1, TimeUnit.MINUTES);
    }

    /**
     * 模拟读取文件内容并将每行数据传入流水线处理
     *
     * @param pipeline 流水线实例
     * @throws InterruptedException 如果在等待过程中被中断则抛出此异常
     */
    private void readFile(SimplePipeline<String, Long> pipeline) throws InterruptedException {
        String line;
        for (int i = 1; i <= fileLineNum; i++) {
            line = "100" + i + "-" + new Random().nextInt(999);
            System.out.println("当前读取到的文件行内容===>>>" + line);
            pipeline.process(line);
        }
    }

    /**
     * 构建完整的流水线结构
     * <p>
     * 初始化线程池，并向流水线中添加三个阶段的处理器(pipe)，分别用于拆分行数据、数值转换及最终展示。
     * </p>
     *
     * @return 配置好的 {@link SimplePipeline} 实例
     */
    private SimplePipeline<String, Long> buildPipeline() {
        final ExecutorService executor = new ThreadPoolExecutor(
                1,
                1,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1024),
                (r) -> {
                    Thread t = new Thread(r, "thread-execute-pipeline");
                    t.setDaemon(true);
                    return t;
                },
                new ThreadPoolExecutor.CallerRunsPolicy());

        final SimplePipeline<String, Long> pipeline = new SimplePipeline<>(executor);

        // 模拟读取日志文件每一行数据，对读取到的每一行数据进行拆分，输入String，输出String
        Pipe<String, String> splitFilePipe = splitFilePipe();
        pipeline.addAsWorkerThreadBasedPipe(splitFilePipe, 1);

        // 模拟统计计算，输入String，输出Long
        Pipe<String, Long> longPipe = buildLongPipe();
        pipeline.addAsWorkerThreadBasedPipe(longPipe, 1);

        // 模拟推送到大屏，输入Long，输出Long
        Pipe<Long, Long> pushPipe = buildPushPipe();
        pipeline.addAsWorkerThreadBasedPipe(pushPipe, 1);

        return pipeline;
    }

    /**
     * 构建推送显示阶段的管道处理器
     * <p>
     * 此阶段仅用于打印当前累计统计结果至控制台。
     * </p>
     *
     * @return 处理器实例
     */
    private Pipe<Long, Long> buildPushPipe() {
        return new AbstractPipe<Long, Long>() {
            @Override
            protected Long doProcess(Long input) throws PipeException {
                System.out.println("当前统计出的结果数据为===>>>" + input);
                return input;
            }
        };
    }

    /**
     * 构建数值解析和累计阶段的管道处理器
     * <p>
     * 将字符串形式的数字转为Long类型，并将其加入全局计数器中。
     * </p>
     *
     * @return 处理器实例
     */
    private Pipe<String, Long> buildLongPipe() {
        return new AbstractPipe<String, Long>() {
            @Override
            protected Long doProcess(String input) throws PipeException {
                return analysisResult.addAndGet(Long.parseLong(input));
            }
        };
    }

    /**
     * 构建文件行拆分阶段的管道处理器
     * <p>
     * 假设原始行为 "prefix-number" 形式，本阶段提取其中的 number 部分。
     * </p>
     *
     * @return 处理器实例
     */
    private Pipe<String, String> splitFilePipe() {
        return new AbstractPipe<String, String>() {
            @Override
            protected String doProcess(String input) throws PipeException {
                // 模拟读取日志文件并分割日志
                return input.split("-")[1];
            }
        };
    }
}
