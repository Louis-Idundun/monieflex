package com.sq018.monieflex.entities;

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
    private String number;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "reference")
    @NotEmpty(message = "Reference should not be empty")
    private String reference;

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
