package com.fitmate.domain.account.entity;

import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.account.vo.Password;
import com.fitmate.domain.account.vo.PrivateInfo;
import com.fitmate.domain.account.vo.ProfileInfo;
import com.fitmate.domain.account.enums.AccountRole;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Getter
@Builder
@SQLDelete(sql = "UPDATE ACCOUNT SET DELETED_AT = CURRENT_TIMESTAMP WHERE ACCOUNT_ID = ? ")
@Where(clause = "DELETED_AT IS NULL")
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
    @NotNull
    private PrivateInfo privateInfo;

    @Embedded
    private ProfileInfo profileInfo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public String getEmail() {
        return this.privateInfo.getEmail();
    }

    public void modifyProfile(Password password, PrivateInfo privateInfo, ProfileInfo profileInfo) {
        if(password != null) {
            this.password = password;
        }
        if(privateInfo != null) {
            this.privateInfo.modifyPrivateInfo(privateInfo);
        }
        if(profileInfo != null) {
            this.profileInfo.modifyProfileInfo(profileInfo);
        }
    }
}
