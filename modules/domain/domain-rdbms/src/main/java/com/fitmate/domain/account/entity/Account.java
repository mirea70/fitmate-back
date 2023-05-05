package com.fitmate.domain.account.entity;

import com.fitmate.enums.AccountRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5)
    @Size(min = 3, max = 5)
    @NotNull
    private String name;

    @Column
    @NotNull
    private String loginName;

    @Column
    @NotNull
    @Size(min = 8)
    private String password;

    @Column
    @NotNull
    @Size(min = 2)
    private String nickName;

    @Column(length = 11)
    @NotNull
    private String phone;

    @Column
    @NotNull
    @Email
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private AccountRole role;
}
