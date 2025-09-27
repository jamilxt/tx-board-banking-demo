package com.sdlc.pro.txboard.demo.bank.service;

import com.sdlc.pro.txboard.demo.bank.entity.Account;
import com.sdlc.pro.txboard.demo.bank.entity.LedgerEntry;
import com.sdlc.pro.txboard.demo.bank.repository.AccountRepository;
import com.sdlc.pro.txboard.demo.bank.repository.LedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccrualService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    // Scenario 8: TransactionTemplate usage
    public void postDailyInterest(TransactionTemplate transactionTemplate) {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            // This will be captured by Spring Tx Board even though method is not @Transactional
            List<Account> activeAccounts = accountRepository.findActiveAccounts();

            for (Account account : activeAccounts) {
                // Calculate 0.01% daily interest
                BigDecimal interest = account.getBalance().multiply(new BigDecimal("0.0001"));

                if (interest.compareTo(BigDecimal.ZERO) > 0) {
                    // Credit interest to account
                    account.setBalance(account.getBalance().add(interest));
                    accountRepository.save(account);

                    // Create ledger entry
                    ledgerEntryRepository.save(new LedgerEntry(account,
                        LedgerEntry.EntryType.CREDIT, interest, "Daily Interest"));
                }
            }
        });
    }
}
