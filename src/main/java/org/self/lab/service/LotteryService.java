package org.self.lab.service;

import cn.hutool.core.collection.CollectionUtil;
import jakarta.annotation.Resource;
import org.self.lab.common.ServiceResult;
import org.self.lab.exception.SelfBusinessException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 */
@Service
public class LotteryService {


    @Lazy
    @Resource
    private RedisTemplate<String, String> redisTemplate;



    // 库存总数
    private static final String STOCK_KEY = "lottery:stock:total";

    private static final Integer STOCK_TOTAL = 100;


    private static final String LOCK_KEY = "lottery:lock";

    /**
     * 抽奖逻辑
     * 中奖状态随机分布在奖品总数内的下标中 对应用户请求(拿到锁)的顺序
     */
    public ServiceResult<String> userDoLottery(Integer userId) {
        try {
            String lotteryTotal = redisTemplate.opsForValue().get(STOCK_KEY);
            if (null == lotteryTotal) throw new SelfBusinessException("已核销奖品数量异常");
            if (Integer.parseInt(lotteryTotal) >= STOCK_TOTAL) {
                return ServiceResult.success("抽奖已结束");
            }
            //当前用户尝试获取抽奖锁
            boolean b = tryToGetLock();
            if(!b){
                return ServiceResult.success("当前活动太火爆 请稍后再试");
            }
            Long userOrder = redisTemplate.opsForList().rightPush("lottery:user:id", userId + "");
            if (null == userOrder) throw new SelfBusinessException("用户当次请求无效");
            // 获取奖品分布
            List<String> lotteryDistribution = getLotteryDistribution();

            if(lotteryDistribution.get(userOrder.intValue()).equals("true")){
                return ServiceResult.success("中奖成功");
            }
            return ServiceResult.success("未中奖");
        }finally {
            redisTemplate.delete(LOCK_KEY);
        }

    }



    private List<String> getLotteryDistribution() {
        List<String> range = redisTemplate.opsForList().range("lottery:distribution", 0, -1);
        if(CollectionUtil.isNotEmpty( range)) return range;

        List<String> distribution = Stream.generate(() -> "false").limit(500).collect(Collectors.toList());

        Random random = new Random();
        IntStream.range(0,STOCK_TOTAL).forEach(i -> {
            boolean tag = true;
            while (tag) {
                int index = random.nextInt(distribution.size());
                if (distribution.get(index).equals("false")) {
                    distribution.set(index, "true");
                    tag = false;
                }
            }
        });

        redisTemplate.opsForList().rightPushAll("ottery:distribution", distribution);
        return distribution;
    }




    private boolean tryToGetLock()  {

        for(int i=0;i<10;i++){

            Boolean b = redisTemplate.opsForValue().setIfAbsent(LOCK_KEY, 1+"");
            if(Boolean.TRUE.equals( b)){
                return true;
            }
            try {
                // 等待20毫秒 循环10次执行 预测一个用户完成抽奖时间为200毫秒 在此期间不断尝试获取锁
                Thread.sleep(20);
            }catch (Exception ignore){}
        }
        return false;
    }

}
