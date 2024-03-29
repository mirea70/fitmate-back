package com.fitmate.domain.account.entity;

import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.account.vo.Password;
import com.fitmate.domain.account.vo.PrivateInfo;
import com.fitmate.domain.account.vo.ProfileInfo;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.converter.SetConverter;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @Column
    @Convert(converter = SetConverter.class)
    @Builder.Default
    private Set<Long> followingList = new HashSet<>();

    @Column
    @Convert(converter = SetConverter.class)
    @Builder.Default
    private Set<Long> followerList = new HashSet<>();

    public String getEmail() {
        return this.privateInfo.getEmail();
    }

    public void modifyProfile(PrivateInfo privateInfo, ProfileInfo profileInfo) {
        if(privateInfo != null) {
            this.privateInfo.modifyPrivateInfo(privateInfo);
        }
        if(profileInfo != null) {
            this.profileInfo.modifyProfileInfo(profileInfo);
        }
    }

    public boolean isFollowing(Long targetId) {
        if(targetId == null) return false;
        if(this.followingList == null || this.followingList.isEmpty()) return false;

        return this.followingList.contains(targetId);
    }

    public void addFollowing(Long targetId) {
        if(targetId == null) return;
        if(this.followingList == null) this.followingList = new HashSet<>();
        this.followingList.add(targetId);
    }

    public void removeFollowing(Long targetId) {
        if(targetId == null) return;
        if(this.followingList == null || this.followingList.isEmpty()) return;
        this.followingList.remove(targetId);
    }

    public void addFollower(Long followerId) {
        if(followerId == null) return;
        if(this.followerList == null) this.followerList = new HashSet<>();
        this.followerList.add(followerId);
    }

    public void removeFollower(Long followerId) {
        if(followerId == null) return;
        if(this.followerList == null || this.followerList.isEmpty()) return;
        this.followerList.remove(followerId);
    }
}
