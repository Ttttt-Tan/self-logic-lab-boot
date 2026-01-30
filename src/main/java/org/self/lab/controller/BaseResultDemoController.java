package org.self.lab.controller;

import com.alibaba.fastjson2.JSONObject;
import org.self.lab.common.BaseResult;
import org.self.lab.common.ServiceResult;
import org.self.lab.service.CommonServiceResultDemoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *  统一controller返回对象场景Demo
 */

@RestController
@RequestMapping("/baseResultDemo")
public class BaseResultDemoController {



    @Autowired
    private CommonServiceResultDemoServiceImpl commonServiceResultDemoService;


    public BaseResult howToUseBaseResultForVoid( ){
        return new BaseResult();
    }

    public BaseResult howToUseBaseResultForResult( ){
        ServiceResult<JSONObject> test = commonServiceResultDemoService.howToUseServiceResult(1, "TEST");
        return new BaseResult(test);
    }


}
