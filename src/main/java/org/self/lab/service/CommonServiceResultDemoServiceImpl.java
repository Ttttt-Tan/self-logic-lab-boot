package org.self.lab.service;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.self.lab.common.ResultCodeEnum;
import org.self.lab.common.ServicePageDataResult;
import org.self.lab.common.ServiceResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 统一service返回对象场景Demo
 * 什么场景应该用哪个ServiceResult的静态方法
 */
@Service
public class CommonServiceResultDemoServiceImpl {


    public ServiceResult<JSONObject> howToUseServiceResult(Integer id, String name) {
        try {
            if (null == id || StringUtils.isBlank(name)) {
                return ServiceResult.failure(ResultCodeEnum.PARAM_IS_NULL);
            }
            if (name.length() > 50) {
                return ServiceResult.failure(ResultCodeEnum.PARAM_CHECK_FAIL);
            }
            if (Objects.equals("SB", name)) {
                return ServiceResult.customizeFailure("敏感词汇");
            }

            JSONObject obj = new JSONObject();

            if (obj.containsKey(name) && id.equals(obj.getIntValue(name))) {
                // 匹配到结果返回
                return ServiceResult.success(obj);
            }
            return ServiceResult.success();
        } catch (Exception e) {
            return ServiceResult.failure();
        }
    }


    public ServiceResult<ServicePageDataResult<JSONObject>> howToUseServicePageDataResult(Integer userId) {
        Integer count = getCount(userId);
        if (Objects.equals(0, userId / 2)) {
            return ServiceResult.success(ServicePageDataResult.empty());
        }
        List<JSONObject> list = getList(userId);

        ServicePageDataResult<JSONObject> jsonObjectServicePageDataResult = new ServicePageDataResult<>(count, list);

        return ServiceResult.success(jsonObjectServicePageDataResult);
    }

    private Integer getCount(Integer userId) {
        return userId / 2;
    }

    private List<JSONObject> getList(Integer userId) {
        return new ArrayList<>();
    }

}
