package com.sq018.monieflex.repositories;

import com.sq018.monieflex.entities.transactions.VerifyFundWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface VerifyFundWalletRepository extends JpaRepository<VerifyFundWallet, Long> {
    List<VerifyFundWallet> findByIsUsedAndUser_EmailAddress(@NonNull Boolean isUsed, @NonNull String emailAddress);
}