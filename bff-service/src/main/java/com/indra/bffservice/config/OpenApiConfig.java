package com.indra.bffservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bffServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("BFF Service API")
                        .description("Backend For Frontend Service API")
                        .version("v1.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("BFF Service Documentation")
                        .url("https://example.com/docs"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("bff")
                .pathsToMatch("/bff/**")
                .build();
    }
}
