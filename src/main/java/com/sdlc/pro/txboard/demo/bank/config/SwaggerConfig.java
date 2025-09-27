package com.sdlc.pro.txboard.demo.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sdlc.pro.txboard.demo.bank.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Banking Demo API")
                .description("Banking demo application to showcase Spring Tx Board features. " +
                        "This API demonstrates various transaction scenarios including healthy transfers, " +
                        "slow operations, connection handling, rollbacks, nested transactions, and more.")
                .version("1.0.0")
                .contact(new Contact("Spring Tx Board Demo",
                        "https://github.com/Mamun-Al-Babu-Shikder/spring-tx-board", ""))
                .build();
    }
}
