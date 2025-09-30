package com.sdlc.pro.txboard.demo.bank.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;

@Table("accounts")
public class Account {
    @Id
    private Long id;

    @Column("customer_id")
    private Long customerId;

    @Column("iban")
    private String iban;

    @Column("balance")
    private BigDecimal balance;

    @Column("currency")
    private String currency;

    @Column("status")
    private AccountStatus status;

    public Account() {}

    public Account(Long customerId, String iban, BigDecimal balance, String currency, AccountStatus status) {
        this.customerId = customerId;
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.status = status;
    }

    public enum AccountStatus {
        ACTIVE, FROZEN, CLOSED
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
