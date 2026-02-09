package org.self.lab.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 *
 */
@Service
@Slf4j
public class MdcAsyncPrintTestService {


    @Resource(name = "bizAsyncExecutor")
    private ThreadPoolTaskExecutor bizAsyncExecutor;

    public void testPrint() {

        log.info("main thread print");


        IntStream.range(0, 10).forEach(i -> {
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(10000000L *i);
                } catch (Exception ignore) {}
                log.info("sync thread print,order:{}", i);
            },bizAsyncExecutor);
        });


    }
}
