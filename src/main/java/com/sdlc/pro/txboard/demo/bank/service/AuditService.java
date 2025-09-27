package com.sdlc.pro.txboard.demo.bank.service;

import com.sdlc.pro.txboard.demo.bank.entity.AuditLog;
import com.sdlc.pro.txboard.demo.bank.entity.Transfer;
import com.sdlc.pro.txboard.demo.bank.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    // Audit with REQUIRES_NEW to create separate transaction
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordTransfer(Transfer transfer) {
        // This creates a separate transaction in the nested tree
        AuditLog auditLog = new AuditLog(transfer,
            String.format("Transfer completed: %s -> %s, Amount: %s",
                transfer.getFromAccount().getIban(),
                transfer.getToAccount().getIban(),
                transfer.getAmount()));

        auditLogRepository.save(auditLog);
    }
}
