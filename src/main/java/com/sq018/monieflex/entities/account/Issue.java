package com.sq018.monieflex.entities.account;

import com.sq018.monieflex.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "issues")
public class Issue extends BaseEntity {
    @Column(name = "issue")
    private String issue;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "user_issue_fkey"
            )
    )
    private User user;
}
