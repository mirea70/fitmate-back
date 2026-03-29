package com.fitmate.adapter.out.persistence.jpa.follow.entity;

import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "follow")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"fromAccountId", "toAccountId"})
public class FollowJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_account_id", insertable = false, updatable = false)
    private Long fromAccountId;

    @Column(name = "to_account_id", insertable = false, updatable = false)
    private Long toAccountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", nullable = false)
    private AccountJpaEntity fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id", nullable = false)
    private AccountJpaEntity toAccount;

    public FollowJpaEntity(AccountJpaEntity fromAccount, AccountJpaEntity toAccount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }
}
