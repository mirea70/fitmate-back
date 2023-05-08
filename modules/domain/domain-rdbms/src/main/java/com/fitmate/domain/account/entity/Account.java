package com.fitmate.domain.account.entity;

import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.account.vo.Password;
import com.fitmate.domain.account.vo.PrivateInfo;
import com.fitmate.domain.account.vo.ProfileInfo;
import com.fitmate.domain.account.enums.AccountRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "id")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String loginName;

    @Embedded
    private Password password;

    @Embedded
    private PrivateInfo privateInfo;

    @Embedded
    private ProfileInfo profileInfo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    public String getEmail() {
        return this.privateInfo.getEmail();
    }

    @Builder
    public Account(String loginName, Password password, PrivateInfo privateInfo,
                   ProfileInfo profileInfo, Gender gender, AccountRole role) {
        this.loginName = loginName;
        this.password = password;
        this.privateInfo = privateInfo;
        this.profileInfo = profileInfo;
        this.gender = gender;
        this.role = role;
    }
}
