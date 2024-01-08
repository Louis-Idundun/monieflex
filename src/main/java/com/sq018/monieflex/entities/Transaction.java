package com.sq018.monieflex.entities;

import com.sq018.monieflex.enums.BillType;
import com.sq018.monieflex.enums.TransactionStatus;
import com.sq018.monieflex.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Transaction extends BaseEntity {
    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "account")
    private String account;

    @Column(name = "receiving_bank_name")
    private String receivingBankName;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "transaction_type")
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "provider_reference")
    private String providerReference;

    @Column(name = "reference")
    @NotEmpty(message = "Reference should not be empty")
    private String reference;

    @Column(name = "narration")
    private String narration;

    @Column(name = "variation")
    private String billVariation;

    @Column(name = "bill_type")
    @Enumerated(value = EnumType.STRING)
    private BillType billType;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "transaction_user_fkey")
    )
    private User user;
}
