package com.sq018.monieflex.repositories;

import com.sq018.monieflex.entities.transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser_EmailAddress(String emailAddress);
}