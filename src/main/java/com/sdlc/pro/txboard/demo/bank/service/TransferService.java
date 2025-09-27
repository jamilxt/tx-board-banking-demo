package com.sdlc.pro.txboard.demo.bank.service;

import com.sdlc.pro.txboard.demo.bank.entity.*;
import com.sdlc.pro.txboard.demo.bank.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    @Autowired
    private DataSource dataSource;

    // Scenario 1: Healthy transfer (INFO logging)
    @Transactional
    public Transfer transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("From account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("To account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // Create transfer record
        Transfer transfer = new Transfer(fromAccount, toAccount, amount, description);
        transfer = transferRepository.save(transfer);

        // Debit from source account
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);

        // Credit to destination account
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(toAccount);

        // Create ledger entries
        ledgerEntryRepository.save(new LedgerEntry(fromAccount, LedgerEntry.EntryType.DEBIT, amount, "Transfer #" + transfer.getId()));
        ledgerEntryRepository.save(new LedgerEntry(toAccount, LedgerEntry.EntryType.CREDIT, amount, "Transfer #" + transfer.getId()));

        // Update transfer status
        transfer.setStatus(Transfer.TransferStatus.COMPLETED);
        return transferRepository.save(transfer);
    }

    // Scenario 2: Slow transfer (WARN on transaction duration)
    @Transactional
    public Transfer transferSlow(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("From account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("To account not found"));

        // Simulate slow anti-fraud check
        try {
            Thread.sleep(600); // Exceeds 500ms threshold
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // Create transfer record
        Transfer transfer = new Transfer(fromAccount, toAccount, amount, description);
        transfer = transferRepository.save(transfer);

        // Debit from source account
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);

        // Credit to destination account
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(toAccount);

        // Create ledger entries
        ledgerEntryRepository.save(new LedgerEntry(fromAccount, LedgerEntry.EntryType.DEBIT, amount, "Slow Transfer #" + transfer.getId()));
        ledgerEntryRepository.save(new LedgerEntry(toAccount, LedgerEntry.EntryType.CREDIT, amount, "Slow Transfer #" + transfer.getId()));

        // Update transfer status
        transfer.setStatus(Transfer.TransferStatus.COMPLETED);
        return transferRepository.save(transfer);
    }

    // Scenario 3: Long-held connection (WARN on connection occupancy)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Transfer transferHoldingConnection(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("From account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("To account not found"));

        // Acquire connection early and hold it
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT balance FROM accounts WHERE id = ? FOR UPDATE");
            stmt.setLong(1, fromAccountId);
            stmt.executeQuery();

            // Hold connection for extended period
            Thread.sleep(300); // Exceeds 250ms connection threshold

        } catch (SQLException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("Connection handling error", e);
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // Create transfer record
        Transfer transfer = new Transfer(fromAccount, toAccount, amount, description);
        transfer = transferRepository.save(transfer);

        // Debit from source account
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);

        // Credit to destination account
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountRepository.save(toAccount);

        // Create ledger entries
        ledgerEntryRepository.save(new LedgerEntry(fromAccount, LedgerEntry.EntryType.DEBIT, amount, "Connection Hold Transfer #" + transfer.getId()));
        ledgerEntryRepository.save(new LedgerEntry(toAccount, LedgerEntry.EntryType.CREDIT, amount, "Connection Hold Transfer #" + transfer.getId()));

        // Update transfer status
        transfer.setStatus(Transfer.TransferStatus.COMPLETED);
        return transferRepository.save(transfer);
    }

    // Scenario 4: Rollback/Error paths
    @Transactional
    public Transfer transferAndFail(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("From account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("To account not found"));

        // Create transfer record
        Transfer transfer = new Transfer(fromAccount, toAccount, amount, description);
        transfer = transferRepository.save(transfer);

        // Debit from source account
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountRepository.save(fromAccount);

        // Create debit ledger entry
        ledgerEntryRepository.save(new LedgerEntry(fromAccount, LedgerEntry.EntryType.DEBIT, amount, "Failed Transfer #" + transfer.getId()));

        // Simulate failure after some operations
        throw new RuntimeException("Transfer failed after debit - will be rolled back");
    }
}
