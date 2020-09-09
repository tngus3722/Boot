package com.example.demo.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EnableSwagger2
@Configuration
public class SwaggerConfig {


    @Bean
    public Docket apiV1(){
        String version = "1.0.0";
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(version))
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
                .paths(Predicates.or(PathSelectors.regex("^(?!/v2).+"))).build().
                        securitySchemes(Arrays.asList(apiKey()));
    }

    @Bean
    public Docket apiV2(){
        String version = "2.0.0";

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(version))
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
                .paths(Predicates.or(PathSelectors.regex("^(?!/v1).+"))).build().
                        securitySchemes(Arrays.asList(apiKey()));
    }

    /*
     * swagger에서는 header로 받고 싶으면 설정을 추가해줘야 한다.
     */
    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }
    private ApiInfo apiInfo(String version) {
        return new ApiInfoBuilder()
                .title("spring boot test" + " " +version)
                .description("test test test test test")
                .version(version)
                .build();
    }
}
