package org.self.lab.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Self-Logic-Lab-Boot 接口文档")
                        .version("1.0.0")
                        .description("集成 MDC 日志追踪、参数防篡改校验的高性能脚手架。")
                        .contact(new Contact().name("tanFenHui")));
    }
}
