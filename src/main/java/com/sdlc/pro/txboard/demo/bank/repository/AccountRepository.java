package com.sdlc.pro.txboard.demo.bank.repository;

import com.sdlc.pro.txboard.demo.bank.entity.Account;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository extends R2dbcRepository<Account, Long> {

    Mono<Account> findByIban(String iban);

    Flux<Account> findByCustomerId(Long customerId);

    @Query("SELECT * FROM accounts WHERE status = 'ACTIVE'")
    Flux<Account> findActiveAccounts();
}
