package com.sdlc.pro.txboard.demo.bank.repository;

import com.sdlc.pro.txboard.demo.bank.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query("SELECT t FROM Transfer t ORDER BY t.createdAt DESC")
    List<Transfer> findAllOrderByCreatedAtDesc();

    @Query("SELECT t FROM Transfer t WHERE t.status = 'COMPLETED' ORDER BY t.createdAt DESC")
    List<Transfer> findCompletedTransfers();
}
