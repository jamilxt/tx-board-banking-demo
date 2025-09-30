package com.sdlc.pro.txboard.demo.bank.config;

import com.sdlc.pro.txboard.demo.bank.entity.*;
import com.sdlc.pro.txboard.demo.bank.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    @Override
    public void run(String... args) throws Exception {
        customerRepository.count()
                .flatMap(count -> {
                    if (count == 0) {
                        return initializeData();
                    }
                    return Mono.empty();
                })
                .doOnSuccess(result -> System.out.println("Demo data initialized successfully!"))
                .block(); // Block here since CommandLineRunner requires synchronous execution
    }

    private Mono<Void> initializeData() {
        // Create customers first
        return Mono.when(
                customerRepository.save(new Customer("John Doe")),
                customerRepository.save(new Customer("Jane Smith")),
                customerRepository.save(new Customer("Bob Johnson")),
                customerRepository.save(new Customer("Alice Williams")),
                customerRepository.save(new Customer("Charlie Brown"))
        ).then(
                // Create accounts using customer IDs (1-5)
                Mono.when(
                        accountRepository.save(new Account(1L, "US12345678901234567890", new BigDecimal("1500.00"), "USD", Account.AccountStatus.ACTIVE)),
                        accountRepository.save(new Account(1L, "US12345678901234567891", new BigDecimal("2500.00"), "USD", Account.AccountStatus.ACTIVE)),
                        accountRepository.save(new Account(2L, "GB29NWBK60161331926819", new BigDecimal("3200.00"), "GBP", Account.AccountStatus.ACTIVE)),
                        accountRepository.save(new Account(2L, "GB29NWBK60161331926820", new BigDecimal("800.00"), "GBP", Account.AccountStatus.ACTIVE)),
                        accountRepository.save(new Account(3L, "DE89370400440532013000", new BigDecimal("5000.00"), "EUR", Account.AccountStatus.ACTIVE)),
                        accountRepository.save(new Account(3L, "DE89370400440532013001", new BigDecimal("1200.00"), "EUR", Account.AccountStatus.ACTIVE)),
                        accountRepository.save(new Account(4L, "FR1420041010050500013M02606", new BigDecimal("4500.00"), "EUR", Account.AccountStatus.ACTIVE)),
                        accountRepository.save(new Account(5L, "IT60X0542811101000000123456", new BigDecimal("750.00"), "EUR", Account.AccountStatus.ACTIVE))
                )
        ).then(
                // Create initial ledger entries (opening balances) using account IDs (1-8)
                Mono.when(
                        ledgerEntryRepository.save(new LedgerEntry(1L, LedgerEntry.EntryType.CREDIT, new BigDecimal("1500.00"), "Opening Balance")),
                        ledgerEntryRepository.save(new LedgerEntry(2L, LedgerEntry.EntryType.CREDIT, new BigDecimal("2500.00"), "Opening Balance")),
                        ledgerEntryRepository.save(new LedgerEntry(3L, LedgerEntry.EntryType.CREDIT, new BigDecimal("3200.00"), "Opening Balance")),
                        ledgerEntryRepository.save(new LedgerEntry(4L, LedgerEntry.EntryType.CREDIT, new BigDecimal("800.00"), "Opening Balance")),
                        ledgerEntryRepository.save(new LedgerEntry(5L, LedgerEntry.EntryType.CREDIT, new BigDecimal("5000.00"), "Opening Balance")),
                        ledgerEntryRepository.save(new LedgerEntry(6L, LedgerEntry.EntryType.CREDIT, new BigDecimal("1200.00"), "Opening Balance")),
                        ledgerEntryRepository.save(new LedgerEntry(7L, LedgerEntry.EntryType.CREDIT, new BigDecimal("4500.00"), "Opening Balance")),
                        ledgerEntryRepository.save(new LedgerEntry(8L, LedgerEntry.EntryType.CREDIT, new BigDecimal("750.00"), "Opening Balance"))
                )
        ).then();
    }
}
