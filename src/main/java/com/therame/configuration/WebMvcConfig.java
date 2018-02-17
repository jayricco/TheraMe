package com.therame.configuration;

import com.therame.service.ViewInterceptorService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Exclude REST API requests, we only need this for controllers that return a view
        // Also exclude /login, because it would be pointless
        registry.addInterceptor(new ViewInterceptorService()).excludePathPatterns("/api/**", "/login");
    }

}
