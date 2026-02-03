package org.self.lab.config;

import org.self.lab.interceptor.ParamValidationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**

 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public ParamValidationInterceptor paramValidationInterceptor() {
        return new ParamValidationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePathPatterns = List.of(
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/webjars/**",
                "/favicon.ico"
        );

        registry.addInterceptor(paramValidationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathPatterns);

    }
}
