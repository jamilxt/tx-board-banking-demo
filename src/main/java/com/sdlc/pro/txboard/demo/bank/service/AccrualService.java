package com.sdlc.pro.txboard.demo.bank.service;

import com.sdlc.pro.txboard.demo.bank.entity.Account;
import com.sdlc.pro.txboard.demo.bank.entity.LedgerEntry;
import com.sdlc.pro.txboard.demo.bank.repository.AccountRepository;
import com.sdlc.pro.txboard.demo.bank.repository.LedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class AccrualService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    // Scenario 8: Daily interest accrual - Reactive version
    @Transactional
    public Mono<String> postDailyInterest() {
        return accountRepository.findActiveAccounts()
                .flatMap(account -> {
                    // Calculate 0.01% daily interest
                    BigDecimal interest = account.getBalance().multiply(new BigDecimal("0.0001"));

                    if (interest.compareTo(BigDecimal.ZERO) > 0) {
                        // Credit interest to account
                        account.setBalance(account.getBalance().add(interest));
                        return accountRepository.save(account)
                                .then(ledgerEntryRepository.save(new LedgerEntry(account.getId(),
                                        LedgerEntry.EntryType.CREDIT, interest, "Daily Interest")))
                                .thenReturn(1);
                    }
                    return Mono.just(0);
                })
                .reduce(0, Integer::sum)
                .map(count -> "Daily interest accrual completed for " + count + " accounts");
    }
}
