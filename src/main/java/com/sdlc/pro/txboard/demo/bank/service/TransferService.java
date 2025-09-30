package com.sdlc.pro.txboard.demo.bank.service;

import com.sdlc.pro.txboard.demo.bank.entity.*;
import com.sdlc.pro.txboard.demo.bank.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;

@Service
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    // Scenario 1: Healthy transfer (INFO logging) - Reactive version
    @Transactional
    public Mono<Transfer> transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        return accountRepository.findById(fromAccountId)
                .switchIfEmpty(Mono.error(new RuntimeException("From account not found")))
                .zipWith(accountRepository.findById(toAccountId)
                        .switchIfEmpty(Mono.error(new RuntimeException("To account not found"))))
                .flatMap(tuple -> {
                    Account fromAccount = tuple.getT1();
                    Account toAccount = tuple.getT2();

                    if (fromAccount.getBalance().compareTo(amount) < 0) {
                        return Mono.error(new RuntimeException("Insufficient funds"));
                    }

                    // Create transfer record
                    Transfer transfer = new Transfer(fromAccountId, toAccountId, amount, description);
                    return transferRepository.save(transfer)
                            .flatMap(savedTransfer -> {
                                // Debit from source account
                                fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                                // Credit to destination account
                                toAccount.setBalance(toAccount.getBalance().add(amount));

                                return accountRepository.save(fromAccount)
                                        .then(accountRepository.save(toAccount))
                                        .then(ledgerEntryRepository.save(new LedgerEntry(fromAccountId, LedgerEntry.EntryType.DEBIT, amount, "Transfer #" + savedTransfer.getId())))
                                        .then(ledgerEntryRepository.save(new LedgerEntry(toAccountId, LedgerEntry.EntryType.CREDIT, amount, "Transfer #" + savedTransfer.getId())))
                                        .then(Mono.fromCallable(() -> {
                                            savedTransfer.setStatus(Transfer.TransferStatus.COMPLETED);
                                            return savedTransfer;
                                        }))
                                        .flatMap(transferRepository::save);
                            });
                });
    }

    // Scenario 2: Slow transfer (WARN on transaction duration) - Reactive version
    @Transactional
    public Mono<Transfer> transferSlow(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        return accountRepository.findById(fromAccountId)
                .switchIfEmpty(Mono.error(new RuntimeException("From account not found")))
                .zipWith(accountRepository.findById(toAccountId)
                        .switchIfEmpty(Mono.error(new RuntimeException("To account not found"))))
                .delayElement(Duration.ofMillis(600)) // Simulate slow anti-fraud check
                .flatMap(tuple -> {
                    Account fromAccount = tuple.getT1();
                    Account toAccount = tuple.getT2();

                    if (fromAccount.getBalance().compareTo(amount) < 0) {
                        return Mono.error(new RuntimeException("Insufficient funds"));
                    }

                    // Create transfer record
                    Transfer transfer = new Transfer(fromAccountId, toAccountId, amount, description);
                    return transferRepository.save(transfer)
                            .flatMap(savedTransfer -> {
                                // Debit from source account
                                fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                                // Credit to destination account
                                toAccount.setBalance(toAccount.getBalance().add(amount));

                                return accountRepository.save(fromAccount)
                                        .then(accountRepository.save(toAccount))
                                        .then(ledgerEntryRepository.save(new LedgerEntry(fromAccountId, LedgerEntry.EntryType.DEBIT, amount, "Slow Transfer #" + savedTransfer.getId())))
                                        .then(ledgerEntryRepository.save(new LedgerEntry(toAccountId, LedgerEntry.EntryType.CREDIT, amount, "Slow Transfer #" + savedTransfer.getId())))
                                        .then(Mono.fromCallable(() -> {
                                            savedTransfer.setStatus(Transfer.TransferStatus.COMPLETED);
                                            return savedTransfer;
                                        }))
                                        .flatMap(transferRepository::save);
                            });
                });
    }

    // Scenario 3: Rollback/Error paths - Reactive version
    @Transactional
    public Mono<Transfer> transferAndFail(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        return accountRepository.findById(fromAccountId)
                .switchIfEmpty(Mono.error(new RuntimeException("From account not found")))
                .zipWith(accountRepository.findById(toAccountId)
                        .switchIfEmpty(Mono.error(new RuntimeException("To account not found"))))
                .flatMap(tuple -> {
                    Account fromAccount = tuple.getT1();

                    // Create transfer record
                    Transfer transfer = new Transfer(fromAccountId, toAccountId, amount, description);
                    return transferRepository.save(transfer)
                            .flatMap(savedTransfer -> {
                                // Debit from source account
                                fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                                return accountRepository.save(fromAccount)
                                        .then(ledgerEntryRepository.save(new LedgerEntry(fromAccountId, LedgerEntry.EntryType.DEBIT, amount, "Failed Transfer #" + savedTransfer.getId())))
                                        .then(Mono.error(new RuntimeException("Transfer failed after debit - will be rolled back")));
                            });
                });
    }
}
