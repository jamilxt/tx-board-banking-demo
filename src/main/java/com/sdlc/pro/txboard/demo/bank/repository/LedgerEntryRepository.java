package com.sdlc.pro.txboard.demo.bank.repository;

import com.sdlc.pro.txboard.demo.bank.entity.LedgerEntry;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface LedgerEntryRepository extends R2dbcRepository<LedgerEntry, Long> {

    Flux<LedgerEntry> findByAccountIdOrderByCreatedAtDesc(Long accountId);

    @Query("SELECT * FROM ledger_entries WHERE account_id = :accountId ORDER BY created_at DESC")
    Flux<LedgerEntry> findRecentEntriesByAccountId(Long accountId);
}
