package org.self.lab.controller;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.self.lab.common.BaseResult;
import org.self.lab.common.ServiceResult;
import org.self.lab.service.CommonServiceResultDemoServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一controller返回对象场景Demo
 */

@RestController
@RequestMapping("/api/baseResultDemo")
@RequiredArgsConstructor
@Tag(name = "演示接口", description = "用于测试 BaseResult")
public class BaseResultDemoController {

    private final CommonServiceResultDemoServiceImpl commonServiceResultDemoService;


    @GetMapping("/howToUseBaseResultForVoid")
    @Operation(summary = "演示如何使用 BaseResult 返回 void")
    public BaseResult howToUseBaseResultForVoid() {
        return new BaseResult();
    }

    @GetMapping("/howToUseBaseResultForResult")
    @Operation(summary = "演示如何使用 BaseResult 返回 ServiceResult")
    public BaseResult howToUseBaseResultForResult() {
        ServiceResult<JSONObject> test = commonServiceResultDemoService.howToUseServiceResult(1, "TEST");
        return new BaseResult(test);
    }


}
