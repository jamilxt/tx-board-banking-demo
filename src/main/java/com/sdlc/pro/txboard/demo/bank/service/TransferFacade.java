package com.sdlc.pro.txboard.demo.bank.service;

import com.sdlc.pro.txboard.demo.bank.entity.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class TransferFacade {

    @Autowired
    private TransferService transferService;

    @Autowired
    private RiskService riskService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private NotificationService notificationService;

    // Scenario 5: Nested transactions (tree in logs) - Reactive version
    @Transactional
    public Mono<Transfer> transferWithNestedServices(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        // Parent transaction - reactive chain
        return riskService.performRiskCheck(fromAccountId, toAccountId, amount)
                .then(transferService.transfer(fromAccountId, toAccountId, amount, description))
                .flatMap(transfer ->
                    auditService.recordTransfer(transfer)
                            .then(notificationService.sendTransferNotification(transfer))
                            .thenReturn(transfer)
                );
    }
}
