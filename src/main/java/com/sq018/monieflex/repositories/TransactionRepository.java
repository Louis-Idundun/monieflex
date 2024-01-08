package com.sq018.monieflex.repositories;

import com.sq018.monieflex.entities.Transaction;
import com.sq018.monieflex.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> queryByUser_EmailAddress(@NonNull String emailAddress);
    Page<Transaction> findByUser_EmailAddress(String emailAddress, Pageable pageable);
    Page<Transaction> findByTransactionTypeAndUser_EmailAddress(TransactionType type, String email, Pageable pageable);
}