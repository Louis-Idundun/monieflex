package com.sq018.monieflex.repositories;

import com.sq018.monieflex.entities.account.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}