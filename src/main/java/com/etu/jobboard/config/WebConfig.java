package com.etu.jobboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Add simple view mappings that do not require a controller
        registry.addViewController("/access-denied").setViewName("error/access-denied");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Customize static resource handling if needed
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
