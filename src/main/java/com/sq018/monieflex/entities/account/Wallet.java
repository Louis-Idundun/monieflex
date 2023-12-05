package com.sq018.monieflex.entities.account;

import com.sq018.monieflex.entities.BaseEntity;
import com.sq018.monieflex.entities.account.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Wallet extends BaseEntity {
    @Column(name = "number")
    @NotEmpty(message = "Wallet should not be empty")
    private BigDecimal number;

    @Column(name = "balance")
    @NotEmpty(message = "Balance should not be empty")
    private BigDecimal balance;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Column(name = "bank_name")
    @NotEmpty(message = "Bank name should not be empty")
    private String bankName;

    @OneToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "wallet_user_fkey")
    )
    private User user;
}
