package com.twat.detalks.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.resource.path.mac}")
    private String resourcePathMac;

    @Value("${file.resource.path.win}")
    private String resourcePathWin;

    @Value("${file.resource.path.nix}")
    private String resourcePathNix;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler(uploadPath)
            .addResourceLocations(resourcePathMac)
            .addResourceLocations(resourcePathWin)
            .addResourceLocations(resourcePathNix);
    }
}
