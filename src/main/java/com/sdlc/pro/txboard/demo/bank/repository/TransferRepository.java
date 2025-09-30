package com.sdlc.pro.txboard.demo.bank.repository;

import com.sdlc.pro.txboard.demo.bank.entity.Transfer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TransferRepository extends R2dbcRepository<Transfer, Long> {

    @Query("SELECT * FROM transfers ORDER BY created_at DESC")
    Flux<Transfer> findAllOrderByCreatedAtDesc();

    @Query("SELECT * FROM transfers WHERE status = 'COMPLETED' ORDER BY created_at DESC")
    Flux<Transfer> findCompletedTransfers();
}
