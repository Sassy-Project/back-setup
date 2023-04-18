package com.projectsassy.sassy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://test.projectsassy.net/", "http://projectsassy.net/", "http://localhost:3007",
                            "https://test.projectsassy.net/", "https://projectsassy.net/", "https://localhost:3007",
                "https://localhost:3000", "https://localhost:3001", "https://localhost:3002", "http://localhost:3000")
            .allowedMethods("GET", "POST", "PATCH", "DELETE")
            .allowedHeaders("Access-Control-Allow-Origin",
                "*",
                "Access-Control-Allow-Methods",
                "GET, POST, PATCH, DELETE",
                "Access-Control-Allow-Headers",
                "Origin, Content-Type, Accept, X-Requested-With")
            .allowCredentials(true)
            .maxAge(3000);
    }

}
