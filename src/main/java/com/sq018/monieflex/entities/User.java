package com.sq018.monieflex.entities;

import com.sq018.monieflex.enums.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Entity(name = "users")
public class User extends BaseEntity implements UserDetails {
    @Column(name = "first_name")
    @NotEmpty(message = "First name should not be empty")
    private String firstName;

    @Column(name = "last_name")
    @NotEmpty(message = "Last name should not be empty")
    private String lastName;

    @Column(name = "encrypted_password")
    @NotEmpty(message = "Password should not be empty")
    private String encryptedPassword;

    @Column(name = "password_recovery")
    private Boolean passwordRecovery = false;

    @Column(name = "transaction_pin")
    private String transactionPin;

    @Column(name = "email_address", unique = true)
    @Email(message = "Email must be properly formatted")
    private String emailAddress;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "phone_number")
    @NotEmpty(message = "Phone number should not be empty")
    private String phoneNumber;

    @Column(name = "bvn")
    @NotEmpty(message = "BVN should not be empty")
    private String bvn;

    @Column(name = "account_status")
    @Enumerated(value = EnumType.STRING)
    private AccountStatus status;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return encryptedPassword;
    }

    @Override
    public String getUsername() {
        return emailAddress;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status == AccountStatus.ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status == AccountStatus.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == AccountStatus.ACTIVE;
    }
}
