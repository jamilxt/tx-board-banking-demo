package com.sdlc.pro.txboard.demo.bank.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("ledger_entries")
public class LedgerEntry {
    @Id
    private Long id;

    @Column("account_id")
    private Long accountId;

    @Column("type")
    private EntryType type;

    @Column("amount")
    private BigDecimal amount;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("reference")
    private String reference;

    public LedgerEntry() {}

    public LedgerEntry(Long accountId, EntryType type, BigDecimal amount, String reference) {
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.reference = reference;
        this.createdAt = LocalDateTime.now();
    }

    public enum EntryType {
        DEBIT, CREDIT
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public EntryType getType() {
        return type;
    }

    public void setType(EntryType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
