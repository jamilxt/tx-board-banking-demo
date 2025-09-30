package com.sdlc.pro.txboard.demo.bank.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;

@Service
public class RiskService {

    // Risk check with REQUIRES_NEW to create separate transaction - Reactive version
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public Mono<Void> performRiskCheck(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        // Simulate risk assessment queries
        // This will create a separate transaction in the nested tree

        if (amount.compareTo(new BigDecimal("10000")) > 0) {
            // Simulate additional checks for large amounts using reactive delay
            return Mono.delay(Duration.ofMillis(50)).then();
        }

        // Risk check passed immediately
        return Mono.empty();
    }
}
