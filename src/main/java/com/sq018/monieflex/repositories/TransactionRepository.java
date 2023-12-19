package com.sq018.monieflex.repositories;

import com.sq018.monieflex.entities.transactions.Transaction;
import com.sq018.monieflex.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByUser_EmailAddress(String emailAddress, Pageable pageable);
    Page<Transaction> findByTransactionTypeAndUser_EmailAddress(TransactionType type, String email, Pageable pageable);
}