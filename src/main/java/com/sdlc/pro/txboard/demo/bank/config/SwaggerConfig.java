package com.sdlc.pro.txboard.demo.bank.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banking Demo API")
                        .version("1.0.0")
                        .description("Banking demo application to showcase Spring Tx Board features. " +
                                "This API demonstrates various transaction scenarios including healthy transfers, " +
                                "slow operations, connection handling, rollbacks, nested transactions, and more.")
                        .contact(new Contact()
                                .name("Spring Tx Board Demo")
                                .url("https://github.com/Mamun-Al-Babu-Shikder/spring-tx-board")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development server")
                ));
    }
}
