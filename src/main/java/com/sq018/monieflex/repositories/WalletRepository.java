package com.sq018.monieflex.repositories;

import com.sq018.monieflex.entities.account.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}