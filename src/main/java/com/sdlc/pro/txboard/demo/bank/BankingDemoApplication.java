package com.sdlc.pro.txboard.demo.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BankingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingDemoApplication.class, args);
        System.out.println("\n=== Banking Demo Application Started (Spring Boot 2.4.0) ===");
        System.out.println("Spring Tx Board UI: http://localhost:8080/tx-board/ui");
        System.out.println("Swagger UI: http://localhost:8080/swagger-ui/");
        System.out.println("API Docs JSON: http://localhost:8080/v2/api-docs");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("Demo endpoints available at: http://localhost:8080/bank/*");
        System.out.println("==========================================================\n");
    }
}
