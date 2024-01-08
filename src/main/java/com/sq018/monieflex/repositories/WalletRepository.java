package com.sq018.monieflex.repositories;

import com.sq018.monieflex.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;


public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUser_EmailAddressIgnoreCase(@NonNull String emailAddress);
    Optional<Wallet> findByNumber(String accountNumber);
}