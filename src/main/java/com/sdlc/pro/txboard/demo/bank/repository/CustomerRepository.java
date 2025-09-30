package com.sdlc.pro.txboard.demo.bank.repository;

import com.sdlc.pro.txboard.demo.bank.entity.Customer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerRepository extends R2dbcRepository<Customer, Long> {

    @Query("SELECT * FROM customers ORDER BY name")
    Flux<Customer> findAllOrderByName();
}
