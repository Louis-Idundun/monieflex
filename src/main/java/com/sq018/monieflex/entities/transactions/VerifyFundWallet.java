package com.sq018.monieflex.entities.transactions;

import com.sq018.monieflex.entities.BaseEntity;
import com.sq018.monieflex.entities.Transaction;
import com.sq018.monieflex.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class VerifyFundWallet extends BaseEntity {

    private String otp;
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    @Column(name = "is_used")
    private Boolean isUsed;

    @OneToOne
    @JoinColumn(
            name = "transaction_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "transaction_id_fkey")
    )
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_id_fkey")
    )
    private User user;

}
