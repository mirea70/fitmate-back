package com.fitmate.adapter.out.persistence.jpa.account.entity;

import com.fitmate.adapter.out.persistence.jpa.common.BaseJpaEntity;
import com.fitmate.adapter.out.persistence.jpa.follow.entity.FollowJpaEntity;
import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.PrivateInfo;
import com.fitmate.domain.account.ProfileInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Column(length = 32, unique = true, nullable = false)
    private String name;

    @Column(length = 37, unique = true, nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private LocalDate birthDate;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String role;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "fromAccount", fetch = FetchType.LAZY)
    private Set<FollowJpaEntity> followings = new HashSet<>();

    @OneToMany(mappedBy = "toAccount", fetch = FetchType.LAZY)
    private Set<FollowJpaEntity> followers = new HashSet<>();

    @Builder
    public AccountJpaEntity(Long id, String loginName, String password, String nickName, String introduction, Long profileImageId, String name, String phone, String email, LocalDate birthDate, String gender, String role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.loginName = loginName;
        this.password = password;
        this.nickName = nickName;
        this.introduction = introduction;
        this.profileImageId = profileImageId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.role = role;
        super.createdAt = createdAt;
        super.updatedAt = updatedAt;
    }

    public static AccountJpaEntity from(Account account) {
        ProfileInfo profileInfo = account.getProfileInfo();
        PrivateInfo privateInfo = account.getPrivateInfo();

        return AccountJpaEntity.builder()
                .id(account.getId().getValue())
                .loginName(account.getLoginName())
                .password(account.getPassword().getValue())
                .nickName(profileInfo.getNickName())
                .introduction(profileInfo.getIntroduction())
                .profileImageId(profileInfo.getProfileImageId())
                .phone(privateInfo.getPhone())
                .email(privateInfo.getEmail())
                .birthDate(privateInfo.getBirthDate())
                .gender(account.getGender().name())
                .role(account.getRole().name())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    public Set<Long> getFollowingIds() {
        return followings.stream()
                .map(FollowJpaEntity::getToAccountId)
                .collect(Collectors.toSet());
    }

    public Set<Long> getFollowerIds() {
        return followers.stream()
                .map(FollowJpaEntity::getFromAccountId)
                .collect(Collectors.toSet());
    }

    public void syncFrom(String password, String nickName, String introduction, Long profileImageId, String name, String phone, String email, LocalDate birthDate) {
        this.password = password;
        this.nickName = nickName;
        this.introduction = introduction;
        this.profileImageId = profileImageId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
    }

    public void syncPassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void anonymizeForDelete() {
        String suffix = "_DEL_" + System.currentTimeMillis();
        this.loginName = this.loginName + suffix;
        this.nickName = this.nickName + suffix;
        this.name = this.name + suffix;
        this.phone = this.phone + suffix;
        this.email = this.email + suffix;
    }
}
