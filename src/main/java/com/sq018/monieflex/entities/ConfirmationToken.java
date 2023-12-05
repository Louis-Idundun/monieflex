package com.sq018.monieflex.entities;

import com.sq018.monieflex.entities.account.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class ConfirmationToken  extends BaseEntity{
    @Column(name = "token")
    @NotEmpty(message = "Token should not be empty")
    @Min(value = 6, message = "Token should not be less than 6 digits")
    private String token;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt = null;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @OneToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "token_user_fkey")
    )
    private User user;
}
