package com.therame.configuration;

import com.therame.service.ViewInterceptorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Properties;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("noreply.therame@gmail.com");
        mailSender.setPassword("hulkhogan");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Exclude REST API requests, we only need this for controllers that return a view
        // Also exclude /login, because it would be pointless
        registry.addInterceptor(new ViewInterceptorService()).excludePathPatterns("/api/**", "/login");
    }

}
