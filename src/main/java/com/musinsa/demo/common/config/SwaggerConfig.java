package com.musinsa.demo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@EnableWebMvc
public class SwaggerConfig {
    private static final String CONTROLLER_BASE_PACKAGE
            = "com.musinsa.demo.controller";

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("무신사 과제 테스트 API 문서")
                .description("보상 지급 서비스 API 목록을 제공 합니다").build();
    }

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .consumes(consumes())
                .produces(produces())
                .apiInfo(swaggerInfo()).select()
                .apis(RequestHandlerSelectors.basePackage(CONTROLLER_BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }

    private Set<String> consumes() {
        Set<String> consumes = new HashSet<>();
        consumes.add(APPLICATION_JSON_VALUE);
        return consumes;
    }

    private Set<String> produces() {
        Set<String> produces = new HashSet<>();
        produces.add(APPLICATION_JSON_VALUE);
        return produces;
    }
}
