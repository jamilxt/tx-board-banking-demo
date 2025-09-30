package com.sdlc.pro.txboard.demo.bank.service;

import com.sdlc.pro.txboard.demo.bank.entity.Customer;
import com.sdlc.pro.txboard.demo.bank.repository.AccountRepository;
import com.sdlc.pro.txboard.demo.bank.repository.CustomerRepository;
import com.sdlc.pro.txboard.demo.bank.repository.LedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class PortfolioService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    // Scenario 7: N+1 detection (WARN) - Reactive version
    @Transactional
    public Mono<Map<String, Object>> customerPortfolioNPlusOne() {
        Map<String, Object> portfolios = new HashMap<>();

        // Load all customers (1 query) and process reactively
        return customerRepository.findAllOrderByName()
                .flatMap(customer -> {
                    Map<String, Object> customerData = new HashMap<>();
                    customerData.put("name", customer.getName());

                    // Load accounts for this customer (creates N+1 pattern)
                    return accountRepository.findByCustomerId(customer.getId())
                            .count()
                            .doOnNext(count -> customerData.put("accountCount", count))
                            .then(accountRepository.findByCustomerId(customer.getId())
                                    .flatMap(account ->
                                        // For each account, load recent ledger entries (N*M queries - simulates N+1 problem)
                                        ledgerEntryRepository.findRecentEntriesByAccountId(account.getId())
                                                .count()
                                    )
                                    .count()
                            )
                            .doOnNext(entryCount -> customerData.put("totalEntries", entryCount))
                            .thenReturn(Map.entry("customer_" + customer.getId(), customerData));
                })
                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                .map(result -> {
                    portfolios.putAll(result);
                    portfolios.put("message", "Portfolio data loaded with N+1 queries pattern");
                    return portfolios;
                });
    }
}
