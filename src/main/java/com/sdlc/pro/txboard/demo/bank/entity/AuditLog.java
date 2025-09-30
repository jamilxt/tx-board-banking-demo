package com.sdlc.pro.txboard.demo.bank.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("audit_logs")
public class AuditLog {
    @Id
    private Long id;

    @Column("transfer_id")
    private Long transferId;

    @Column("message")
    private String message;

    @Column("created_at")
    private LocalDateTime createdAt;

    public AuditLog() {}

    public AuditLog(Long transferId, String message) {
        this.transferId = transferId;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
