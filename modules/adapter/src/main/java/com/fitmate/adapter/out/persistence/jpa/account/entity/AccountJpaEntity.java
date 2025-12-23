package com.fitmate.adapter.out.persistence.jpa.account.entity;

import com.fitmate.adapter.out.converter.SetConverter;
import com.fitmate.adapter.out.persistence.jpa.common.BaseJpaEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account")
@SQLDelete(sql = "UPDATE ACCOUNT SET DELETED_AT = CURRENT_TIMESTAMP WHERE ID = ? ")
@Where(clause = "DELETED_AT IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AccountJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(unique = true, nullable = false)
    private String loginName;

    @Column
    private String password;

    @Column(unique = true, nullable = false)
    private String nickName;

    @Column(length = 50)
    private String introduction;

    @Column
    private Long profileImageId;

    @Column(length = 5,unique = true, nullable = false)
    private String name;

    @Column(length = 11, unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String role;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column
    @Convert(converter = SetConverter.class)
    private Set<Long> followings = new HashSet<>();

    @Column
    @Convert(converter = SetConverter.class)
    private Set<Long> followers = new HashSet<>();

    @Builder
    public AccountJpaEntity(Long id, String loginName, String password, String nickName, String introduction, Long profileImageId, String name, String phone, String email, String gender, String role, Set<Long> followings, Set<Long> followers, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.loginName = loginName;
        this.password = password;
        this.nickName = nickName;
        this.introduction = introduction;
        this.profileImageId = profileImageId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.role = role;
        super.createdAt = createdAt;
        super.updatedAt = updatedAt;
        this.followings = followings;
        this.followers = followers;
    }
}
