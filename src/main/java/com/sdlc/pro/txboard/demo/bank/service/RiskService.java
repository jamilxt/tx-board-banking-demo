package com.sdlc.pro.txboard.demo.bank.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class RiskService {

    // Risk check with REQUIRES_NEW to create separate transaction
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void performRiskCheck(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        // Simulate risk assessment queries
        // This will create a separate transaction in the nested tree

        if (amount.compareTo(new BigDecimal("10000")) > 0) {
            // Simulate additional checks for large amounts
            try {
                Thread.sleep(50); // Small delay for risk processing
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Risk check passed
    }
}
