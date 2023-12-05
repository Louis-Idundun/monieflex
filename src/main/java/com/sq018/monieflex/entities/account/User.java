package com.sq018.monieflex.entities.account;

import com.sq018.monieflex.entities.BaseEntity;
import com.sq018.monieflex.entities.transactions.Beneficiary;
import com.sq018.monieflex.entities.transactions.Transaction;
import com.sq018.monieflex.enums.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity(name = "users")
public class User extends BaseEntity {
    @Column(name = "first_name")
    @NotEmpty(message = "First name should not be empty")
    @Min(value = 2, message = "First name should not be less than 2 characters")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "Last name should not be empty")
    @Min(value = 2, message = "Last name should not be less than 2 characters")
    private String lastName;

    @Column(name = "encrypted_password")
    @NotEmpty(message = "Password should not be empty")
    @Min(value = 2, message = "Password should not be less than 2 characters")
    private String encryptedPassword;

    @Column(name = "transaction_pin")
    @Min(value = 4, message = "Pin should not be less than 4 digits")
    private String transactionPin;

    @Column(name = "email_address")
    @Email(message = "Email must be properly formatted")
    private String emailAddress;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "phone_number")
    @NotEmpty(message = "Phone number should not be empty")
    @Min(value = 11, message = "Phone number should not be less than 11 digits")
    private String phoneNumber;

    @Column(name = "bvn")
    @NotEmpty(message = "BVN should not be empty")
    @Min(value = 2, message = "BVN should not be less than 2 characters")
    private BigDecimal bvn;

    @Column(name = "account_status")
    @Enumerated(value = EnumType.STRING)
    private AccountStatus status;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Beneficiary> beneficiaries;
}
