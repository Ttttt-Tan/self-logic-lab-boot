package org.self.lab.interceptor;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.self.lab.common.SelfConstants;
import org.self.lab.exception.SelfBusinessException;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

/**
 * 对称加密防止参数篡改拦截器
 */
public class ParamValidationInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws Exception {

        String selfCheckTime = request.getParameter(SelfConstants.SELF_CHECK_TIME);
        String selfValidation = request.getParameter(SelfConstants.SELF_VALIDATION);

        if (StringUtils.isBlank(selfCheckTime) || StringUtils.isBlank(selfValidation)) {
            throw new SelfBusinessException("请求安全校验失败:缺少参数签名信息");
        }
        long requestTime = Long.parseLong(selfCheckTime);
        if (Math.abs(System.currentTimeMillis() - requestTime) > 30000) {
            throw new SelfBusinessException("请求安全校验失败：请求已过期");
        }

        Map<String, String> soryMap = new TreeMap<>();

        request.getParameterMap().forEach((key, values) -> {
            // 排除掉签名本身，不参与签名计算
            if (!key.equals(SelfConstants.SELF_VALIDATION)) {
                soryMap.put(key, String.join(",", values));
            }
        });

        if (request instanceof ContentCachingRequestWrapper wrapper) {
            byte[] body = wrapper.getContentAsByteArray();
            if (body.length > 0) {
                String jsonBody = new String(body, StandardCharsets.UTF_8);
                soryMap.put("bodyContent", jsonBody);
            }
        }
        StringBuilder sb = new StringBuilder();
        soryMap.forEach((key, value) -> sb.append(key).append("=").append(value).append("&"));
        sb.append("salt=").append("XXXXXX");

        String calculatedSign = DigestUtils.md5DigestAsHex(sb.toString().getBytes(StandardCharsets.UTF_8));

        if(!calculatedSign.equals(selfValidation)){
            throw new SelfBusinessException("请求安全校验失败：签名未匹配");
        }
        return true;
    }
}
