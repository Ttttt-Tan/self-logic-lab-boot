package org.self.lab.config;

import cn.hutool.core.map.MapUtil;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 */
@Configuration
public class ExecutorConfig {


    // 核心任务线程池
    @Bean("coreTaskExecutor")
    public ThreadPoolTaskExecutor coreTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("core-task-");
        executor.setTaskDecorator(new MdcTaskDecorator());
        // 核心池拒绝策略：抛出异常，触发告警
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

    @Bean("bizAsyncExecutor")
    public ThreadPoolTaskExecutor bizAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);         // 初始不用太高
        executor.setMaxPoolSize(50);        // 弹性空间给大一点
        executor.setQueueCapacity(500);     // 缓冲队列给大一点
        executor.setThreadNamePrefix("biz-async-");
        executor.setTaskDecorator(new MdcTaskDecorator());
        // 业务池拒绝策略：由调用者执行，起到限流效果
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }


    public static class MdcTaskDecorator implements TaskDecorator {
        @Override
        @NonNull
        public Runnable decorate(@NonNull Runnable runnable) {
            // 提交任务时 拷贝当前MDC上下文
            Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();

            return () -> {
                try {
                    // 2 子线程执前 copy
                    if (MapUtil.isNotEmpty(copyOfContextMap)) {
                        MDC.setContextMap(copyOfContextMap);
                    }
                    runnable.run();
                } finally {
                    // 子线程清理
                    MDC.clear();
                }
            };
        }
    }

}
