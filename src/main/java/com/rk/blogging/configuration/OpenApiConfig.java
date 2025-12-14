package com.rk.blogging.configuration;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Blogging API",
                version = "1.0",
                description = "Spring Boot Blogging Platform API with JWT Security",
                contact = @Contact(
                        name = "API Support",
                        email = "support@blogapi.com"
                )
        )
)
public class OpenApiConfig {
}