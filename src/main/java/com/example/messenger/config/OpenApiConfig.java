package com.example.messenger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("Messenger WebSocket API")
                        .description("WebSocket API для мессенджера с поддержкой STOMP протокола")
                        .version("1.1.0")
                        .contact(new Contact()
                                .name("Messenger Team")
                                .url("https://github.com/your-repo")
                                .email("support@messenger.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ))
                // Глобально требуем bearerAuth для всех операций
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
