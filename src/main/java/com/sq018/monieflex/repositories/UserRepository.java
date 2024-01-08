package com.sq018.monieflex.repositories;

import com.sq018.monieflex.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAddress(@NonNull String emailAddress);
}