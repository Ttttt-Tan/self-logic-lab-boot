package org.self.lab.monitor;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 *  线程池实时监控任务
 */
@Component
@Slf4j
public class ThreadPoolMonitor {

    @Resource(name = "coreTaskExecutor")
    private ThreadPoolTaskExecutor coreTaskExecutor;

    @Resource(name = "bizAsyncExecutor")
    private ThreadPoolTaskExecutor  bizAsyncExecutor;

    @Scheduled(fixedRate = 5000)
    public void reportThreadPoolStatus(){
        log.info("[线程池监控] 核心池: {} | 业务池: {}",
                formatStatus(coreTaskExecutor, "核心"),
                formatStatus(bizAsyncExecutor, "业务"));
    }

    /**
     * 格式化线程池状态信息
     */
    private String formatStatus(ThreadPoolTaskExecutor executor, String poolName) {
        // 活跃线程数
        int activeCount = executor.getActiveCount();
        // 最大线程数
        int maxPoolSize = executor.getMaxPoolSize();
        // 队列中等待的任务数
        int queueSize = executor.getThreadPoolExecutor().getQueue().size();
        // 队列剩余容量
        int queueRemaining = executor.getThreadPoolExecutor().getQueue().remainingCapacity();
        // 总队列容量
        int queueCapacity = queueSize + queueRemaining;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("活跃线程 %d/%d, 排队 %d/%d",
                activeCount, maxPoolSize, queueSize, queueCapacity));

        // 预警逻辑：如果排队占了 80% 以上，或者活跃线程达到最大值
        if (queueSize > queueCapacity * 0.8 || activeCount >= maxPoolSize) {
            sb.append("（预警！）");
        }

        return sb.toString();
    }

}
