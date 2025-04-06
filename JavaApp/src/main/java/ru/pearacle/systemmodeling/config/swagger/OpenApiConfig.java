package ru.pearacle.systemmodeling.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI systemModelingServiceOpenApi() {
        return new OpenAPI().info(new Info().title("System-Modeling service API")
                .description("Welcome to API documentation for System-Modeling service")
                .version("v1.0"));
    }
}
