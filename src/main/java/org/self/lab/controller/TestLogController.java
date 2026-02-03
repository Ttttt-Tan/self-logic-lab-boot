package org.self.lab.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.self.lab.common.BaseResult;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/api/logTest")
@Slf4j
public class TestLogController {


    @GetMapping("/get")
    public BaseResult testGet(@RequestParam String id) {
        // 这里业务逻辑写几行日志，看是否带 TraceID
        return new BaseResult();
    }


    @PostMapping("/post")
    public BaseResult testPost(@RequestBody JSONObject user) {
        log.info("controller 进来了 开始执行");
        try {
            Thread.sleep(100);
        } catch (Exception ignore) {
        }
        return new BaseResult();
    }

}
