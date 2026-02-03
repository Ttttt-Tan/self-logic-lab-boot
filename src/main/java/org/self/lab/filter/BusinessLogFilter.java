package org.self.lab.filter;

import cn.hutool.core.map.MapUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.self.lab.common.SelfConstants;
import org.slf4j.MDC;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 全局业务日志过滤器
 */
@Slf4j
public class BusinessLogFilter implements Filter {



    // 限制日志打印的最大长度（如 2KB），防止因大报文导致 OOM (内存溢出)
    private static final int MAX_BODY_SIZE = 2048;


    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 1. 是不是HTTP请求
        if (!(request instanceof HttpServletRequest servletRequest) ||
                !(response instanceof HttpServletResponse servletResponse)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 2. 包装可重复读
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(servletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(servletResponse);

        // 3. MDC 设置
        String traceId = UUID.randomUUID().toString().replace("-", "");
        MDC.put(SelfConstants.SELF_TRACE_ID, traceId);


        long startTime = System.currentTimeMillis();
        try {
            // 4.后续逻辑执行
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            // 5.日志打印
            try {
                logTraceDetails(requestWrapper, responseWrapper, startTime);
            } catch (Exception e) {
                log.error("#BusinessLogFilter 日志链路监控记录失败", e);
            }
            //6. 响应结果写入
            responseWrapper.copyBodyToResponse();

            // 7. MDC 清空
            MDC.remove(SelfConstants.SELF_TRACE_ID);
        }
    }

    /**
     * 提取并打印请求与响应的详细信息
     */
    private void logTraceDetails(ContentCachingRequestWrapper req, ContentCachingResponseWrapper resp, long startTime) {
        long duration = System.currentTimeMillis() - startTime;

        // 获取入参
        StringBuffer requestParam=new StringBuffer();

        Map<String, String[]> parameterMap = req.getParameterMap();
        if(MapUtil.isNotEmpty(parameterMap)){
            List<String> list = parameterMap.entrySet().stream()
                    .map(e -> e.getKey() + ": " + String.join(",", e.getValue()))
                    .toList();
            requestParam.append("Params: [").append(String.join(",", list)).append("] ");
        }
        // 依赖于Controller的@RequestBody读取参数
        String requestBody = getPayload(req.getContentAsByteArray());
        if(!"{}".equals(requestBody)){
            requestParam.append("Body: ").append(requestBody);
        }
        // 获取返回值
        String responseBody = getPayload(resp.getContentAsByteArray());

        log.info("""
        
        {} [Link-Trace] ========================================== {}
        selfTraceId : {}
        URL      : {} {} {} {}
        Status   : {} {} {}
        Time     : {} {} ms {}
        Request  : {}
        Response : {}
        {} ======================================================= {}
        """,
                BLUE,RESET,
                MDC.get(SelfConstants.SELF_TRACE_ID),
                GREEN, req.getMethod(), req.getRequestURI(), RESET,
                YELLOW, resp.getStatus(), RESET,
                RED, duration, RESET,
                requestParam, responseBody,
                BLUE, RESET);
    }


    /**
     * 安全地获取报文字符串，包含长度截断防止内存溢出
     */
    private String getPayload(byte[] buf) {
        if (buf == null || buf.length == 0) {
            return "{}";
        }
        // 长度截断保护
        int length = Math.min(buf.length, MAX_BODY_SIZE);
        try {
            String payload = new String(buf, 0, length, StandardCharsets.UTF_8);
            return buf.length > MAX_BODY_SIZE ? payload + " [Truncated...]" : payload;
        } catch (Exception e) {
            return "[Payload parse error]";
        }
    }
}
