package com.xietong.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by Administrator on 2016/3/25.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
        @Bean
        public Docket apiDocket() {
                return new Docket(DocumentationType.SWAGGER_2)
                        .apiInfo(apiInfo())
                        .select()
                        .apis(RequestHandlerSelectors.any())
                        .paths(regex("/api/.*"))
                        .build()
                        .globalOperationParameters(newArrayList(new ParameterBuilder()
                                .name("access_token")
                                .description("oauth token")
                                .modelRef(new ModelRef("string"))
                                .parameterType("query")
                                .required(false)
                                .build()));
        }

        private ApiInfo apiInfo() {
                return new ApiInfoBuilder()
                        .title("App Management Service")
                        .description("App management related service")
                        .build();
        }
}
