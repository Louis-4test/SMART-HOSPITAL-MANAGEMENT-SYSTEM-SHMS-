package com.hms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Hospital Management System API")
                        .version("1.0.0")
                        .description("REST API for the Smart Hospital Management System")
                        .contact(new Contact().name("Developer").email("dev@hospital.com"))
                        .license(new License().name("Private")))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .schemaRequirement("basicAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic"));
    }

    @Bean
    public WebMvcConfigurer swaggerRedirect() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addRedirectViewController("/swagger-ui/", "/swagger-ui/index.html");
            }
        };
    }
}
