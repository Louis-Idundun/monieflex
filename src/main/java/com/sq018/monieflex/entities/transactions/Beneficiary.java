package com.sq018.monieflex.entities.transactions;

import com.sq018.monieflex.entities.BaseEntity;
import com.sq018.monieflex.entities.account.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Beneficiary extends BaseEntity {
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_number")
    private BigDecimal accountNumber;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "user_bf_fkey")
    )
    private User user;
}
