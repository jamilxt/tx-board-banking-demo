package com.sdlc.pro.txboard.demo.bank.repository;

import com.sdlc.pro.txboard.demo.bank.entity.AuditLog;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AuditLogRepository extends R2dbcRepository<AuditLog, Long> {

    Flux<AuditLog> findByTransferIdOrderByCreatedAtDesc(Long transferId);
}
