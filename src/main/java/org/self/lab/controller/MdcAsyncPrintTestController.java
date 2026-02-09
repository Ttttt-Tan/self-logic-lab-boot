package org.self.lab.controller;

import lombok.RequiredArgsConstructor;
import org.self.lab.annotation.IgnoreParamCheck;
import org.self.lab.common.BaseResult;
import org.self.lab.service.MdcAsyncPrintTestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  测试MDC异步线程输出
 */
@RestController
@RequestMapping("/api/mdc/test")
@RequiredArgsConstructor
public class MdcAsyncPrintTestController {


    private final MdcAsyncPrintTestService mdcAsyncPrintTestService;


    @RequestMapping("/print")
    @IgnoreParamCheck
    public BaseResult print() {
        mdcAsyncPrintTestService.testPrint();
        return new BaseResult();
    }
}
