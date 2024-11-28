package com.sample.base.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
//    @Bean
//    public GroupedOpenApi getItemApi() {
//        return GroupedOpenApi
//                .builder()
//                .group("item")
//                .pathsToMatch("/api/item/**")
//                .build();
//
//    }

    @Bean
    public OpenAPI getOpenApi() {
        return new OpenAPI().components(new Components())
                .info(getInfo());

    }

    private Info getInfo() {
        return new Info()
                .version("1.0.0")
                .description("김재윤의 SpringBoot Api 기본 골격")
                .title("김재윤 SpringBoot Test Application ");
    }
}
