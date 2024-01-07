package com.sq018.monieflex.repositories;

import com.sq018.monieflex.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAddress(@NonNull String emailAddress);

    @Query(value = "select u.first_name, u.last_name from users u join wallet w on u.id=w.user_id where w.number=:accountNumber", nativeQuery = true)
    List<Object[]> findUserByWalletNumber(@Param("accountNumber") String accountNumber);
}