package com.sq018.monieflex.repositories;

import com.sq018.monieflex.entities.account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from users u where u.emailAddress = ?1")
    Optional<User> findByEmailAddress(@NonNull String emailAddress);
}