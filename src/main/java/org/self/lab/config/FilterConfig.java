package org.self.lab.config;

import org.self.lab.filter.BusinessLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class FilterConfig {


    @Bean
    public FilterRegistrationBean<BusinessLogFilter> businessLogFilterRegistration() {
        FilterRegistrationBean<BusinessLogFilter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new BusinessLogFilter());
        registration.addUrlPatterns("/*");
        registration.setName("businessLogFilter");
        registration.setOrder(1);
        return registration;
    }

}
