package com.sdlc.pro.txboard.demo.bank.service;

import com.sdlc.pro.txboard.demo.bank.entity.AuditLog;
import com.sdlc.pro.txboard.demo.bank.entity.Transfer;
import com.sdlc.pro.txboard.demo.bank.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    // Audit with REQUIRES_NEW to create separate transaction - Reactive version
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Mono<Void> recordTransfer(Transfer transfer) {
        // This creates a separate transaction in the nested tree
        AuditLog auditLog = new AuditLog(transfer.getId(),
            String.format("Transfer completed: Account %d -> Account %d, Amount: %s",
                transfer.getFromAccountId(),
                transfer.getToAccountId(),
                transfer.getAmount()));

        return auditLogRepository.save(auditLog).then();
    }
}
