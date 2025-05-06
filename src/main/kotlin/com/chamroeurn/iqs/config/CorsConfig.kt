package com.chamroeurn.iqs.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000") // Allow your Vue.js app
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Include OPTIONS!
            .allowedHeaders("Content-Type", "Authorization") // Include any custom headers
            .allowCredentials(true)
        super.addCorsMappings(registry)
    }
}