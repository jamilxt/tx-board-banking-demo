package com.sdlc.pro.txboard.demo.bank.service;

import com.sdlc.pro.txboard.demo.bank.entity.Account;
import com.sdlc.pro.txboard.demo.bank.entity.Customer;
import com.sdlc.pro.txboard.demo.bank.entity.LedgerEntry;
import com.sdlc.pro.txboard.demo.bank.repository.CustomerRepository;
import com.sdlc.pro.txboard.demo.bank.repository.LedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PortfolioService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    // Scenario 7: N+1 detection (WARN)
    @Transactional
    public Map<String, Object> customerPortfolioNPlusOne() {
        Map<String, Object> portfolios = new HashMap<>();

        // Load all customers (1 query)
        List<Customer> customers = customerRepository.findAllOrderByName();

        // For each customer, load their accounts and recent ledger entries
        // This creates N+1 query pattern
        for (Customer customer : customers) {
            Map<String, Object> customerData = new HashMap<>();
            customerData.put("name", customer.getName());

            // Load accounts for this customer (N queries - one per customer)
            List<Account> accounts = customer.getAccounts();
            customerData.put("accountCount", accounts.size());

            // For each account, load recent ledger entries (N*M queries)
            for (Account account : accounts) {
                List<LedgerEntry> recentEntries = ledgerEntryRepository.findRecentEntriesByAccount(account);
                // This creates multiple queries - one per account
            }

            portfolios.put("customer_" + customer.getId(), customerData);
        }

        return portfolios;
    }
}
