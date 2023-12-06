package com.sq018.monieflex.repositories;

import com.sq018.monieflex.entities.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByUser_EmailAddress(@NonNull String emailAddress);
}