package org.self.lab.controller;

import com.alibaba.fastjson2.JSONObject;
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
public class BaseResultDemoController {

    private final CommonServiceResultDemoServiceImpl commonServiceResultDemoService;


    @GetMapping("/howToUseBaseResultForVoid")
    public BaseResult howToUseBaseResultForVoid() {
        return new BaseResult();
    }

    @GetMapping("/howToUseBaseResultForResult")
    public BaseResult howToUseBaseResultForResult() {
        ServiceResult<JSONObject> test = commonServiceResultDemoService.howToUseServiceResult(1, "TEST");
        return new BaseResult(test);
    }


}
