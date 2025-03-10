package com.Jobs.JobsMemory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    public WebConfig() {
    }

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(new String[]{"https://login-angular-eight.vercel.app/login"}).allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"}).allowedHeaders(new String[]{"*"}).allowCredentials(true);
    }
}
