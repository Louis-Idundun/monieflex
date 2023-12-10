package com.sq018.monieflex.configs;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                    new Info()
                            .title("MonieFlex Api - Banking with ease")
                            .version("1.0")
                            .description("Your mobile banking, best described with ease and swiftness")
                )
                .components(
                        new Components()
                            .addSecuritySchemes(
                                    "Bearer Authentication",
                                    new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("Bearer")
                                        .bearerFormat("JWT")
                            )
                )
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }
}
