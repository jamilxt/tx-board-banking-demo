package com.sdlc.pro.txboard.demo.bank.repository;

import com.sdlc.pro.txboard.demo.bank.entity.Account;
import com.sdlc.pro.txboard.demo.bank.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    List<LedgerEntry> findByAccountOrderByCreatedAtDesc(Account account);

    @Query("SELECT l FROM LedgerEntry l WHERE l.account = :account ORDER BY l.createdAt DESC")
    List<LedgerEntry> findRecentEntriesByAccount(@Param("account") Account account);

    @Query("SELECT l FROM LedgerEntry l WHERE l.account.id = :accountId ORDER BY l.createdAt DESC")
    List<LedgerEntry> findByAccountIdOrderByCreatedAtDesc(@Param("accountId") Long accountId);
}
