package com.sdlc.pro.txboard.demo.bank.repository;

import com.sdlc.pro.txboard.demo.bank.entity.Account;
import com.sdlc.pro.txboard.demo.bank.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByIban(String iban);

    List<Account> findByCustomer(Customer customer);

    @Query("SELECT a FROM Account a WHERE a.status = 'ACTIVE'")
    List<Account> findActiveAccounts();

    @Query("SELECT a FROM Account a WHERE a.balance > 0")
    List<Account> findAccountsWithPositiveBalance();
}
