package com.example.Kiddit.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@org.springframework.lang.NonNull CorsRegistry registry) {
        registry.addMapping("/api/**") 
                .allowedOrigins("http://localhost:4200") 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") 
                .allowCredentials(true); 
    }
}
