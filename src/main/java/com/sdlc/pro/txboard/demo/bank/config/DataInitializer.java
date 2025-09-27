package com.sdlc.pro.txboard.demo.bank.config;

import com.sdlc.pro.txboard.demo.bank.entity.*;
import com.sdlc.pro.txboard.demo.bank.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        if (customerRepository.count() == 0) {
            initializeData();
            System.out.println("Demo data initialized successfully!");
        }
    }

    private void initializeData() {
        // Create customers
        Customer customer1 = customerRepository.save(new Customer("John Doe"));
        Customer customer2 = customerRepository.save(new Customer("Jane Smith"));
        Customer customer3 = customerRepository.save(new Customer("Bob Johnson"));
        Customer customer4 = customerRepository.save(new Customer("Alice Williams"));
        Customer customer5 = customerRepository.save(new Customer("Charlie Brown"));

        // Create accounts with different balances and currencies
        Account account1 = accountRepository.save(new Account(customer1, "US12345678901234567890", new BigDecimal("1500.00"), "USD", Account.AccountStatus.ACTIVE));
        Account account2 = accountRepository.save(new Account(customer1, "US12345678901234567891", new BigDecimal("2500.00"), "USD", Account.AccountStatus.ACTIVE));
        Account account3 = accountRepository.save(new Account(customer2, "GB29NWBK60161331926819", new BigDecimal("3200.00"), "GBP", Account.AccountStatus.ACTIVE));
        Account account4 = accountRepository.save(new Account(customer2, "GB29NWBK60161331926820", new BigDecimal("800.00"), "GBP", Account.AccountStatus.ACTIVE));
        Account account5 = accountRepository.save(new Account(customer3, "DE89370400440532013000", new BigDecimal("5000.00"), "EUR", Account.AccountStatus.ACTIVE));
        Account account6 = accountRepository.save(new Account(customer3, "DE89370400440532013001", new BigDecimal("1200.00"), "EUR", Account.AccountStatus.ACTIVE));
        Account account7 = accountRepository.save(new Account(customer4, "FR1420041010050500013M02606", new BigDecimal("4500.00"), "EUR", Account.AccountStatus.ACTIVE));
        Account account8 = accountRepository.save(new Account(customer5, "IT60X0542811101000000123456", new BigDecimal("750.00"), "EUR", Account.AccountStatus.ACTIVE));

        // Create initial ledger entries (opening balances)
        ledgerEntryRepository.save(new LedgerEntry(account1, LedgerEntry.EntryType.CREDIT, new BigDecimal("1500.00"), "Opening Balance"));
        ledgerEntryRepository.save(new LedgerEntry(account2, LedgerEntry.EntryType.CREDIT, new BigDecimal("2500.00"), "Opening Balance"));
        ledgerEntryRepository.save(new LedgerEntry(account3, LedgerEntry.EntryType.CREDIT, new BigDecimal("3200.00"), "Opening Balance"));
        ledgerEntryRepository.save(new LedgerEntry(account4, LedgerEntry.EntryType.CREDIT, new BigDecimal("800.00"), "Opening Balance"));
        ledgerEntryRepository.save(new LedgerEntry(account5, LedgerEntry.EntryType.CREDIT, new BigDecimal("5000.00"), "Opening Balance"));
        ledgerEntryRepository.save(new LedgerEntry(account6, LedgerEntry.EntryType.CREDIT, new BigDecimal("1200.00"), "Opening Balance"));
        ledgerEntryRepository.save(new LedgerEntry(account7, LedgerEntry.EntryType.CREDIT, new BigDecimal("4500.00"), "Opening Balance"));
        ledgerEntryRepository.save(new LedgerEntry(account8, LedgerEntry.EntryType.CREDIT, new BigDecimal("750.00"), "Opening Balance"));

        System.out.println("Initialized 5 customers with 8 accounts and opening balances");
        System.out.println("Account IDs 1-8 are available for demo transfers");
    }
}
