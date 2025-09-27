package com.sdlc.pro.txboard.demo.bank.service;

import com.sdlc.pro.txboard.demo.bank.entity.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    // Scenario 5: Nested transactions (tree in logs)
    @Transactional
    public Transfer transferWithNestedServices(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        // Parent transaction

        // Call risk service with REQUIRES_NEW
        riskService.performRiskCheck(fromAccountId, toAccountId, amount);

        // Perform the actual transfer
        Transfer transfer = transferService.transfer(fromAccountId, toAccountId, amount, description);

        // Call audit service with REQUIRES_NEW
        auditService.recordTransfer(transfer);

        // Call notification service (non-transactional)
        notificationService.sendTransferNotification(transfer);

        return transfer;
    }
}
