package com.eventura.backend.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi customApi() {
        return GroupedOpenApi.builder()
                .group("custom-api") // Nom du groupe
                .pathsToMatch("/api/**","/actuator/*") // N'expose que les routes commençant par /api
                .build();
    }
}
