package org.self.lab.controller;

import lombok.RequiredArgsConstructor;
import org.self.lab.common.BaseResult;
import org.self.lab.service.LotteryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *  抽奖实现逻辑Demo
 */
@RestController
@RequestMapping("/api/lottery")
@RequiredArgsConstructor
public class lotteryController {


    private final LotteryService lotteryService;


    @RequestMapping("/userDoLottery")
    public BaseResult userDoLottery(@RequestParam Integer userId) {
        return new BaseResult(lotteryService.userDoLottery(userId));
    }

}
