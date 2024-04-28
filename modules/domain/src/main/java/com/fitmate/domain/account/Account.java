package com.fitmate.domain.account;

import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    private final AccountId id;

    private final String loginName;

    private Password password;

    private ProfileInfo profileInfo;

    private PrivateInfo privateInfo;

    private final Gender gender;

    private final AccountRole role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private Set<Long> followings;

    private Set<Long> followers;

    public static Account withId(AccountId id, String loginName, Password password, ProfileInfo profileInfo, PrivateInfo privateInfo, Gender gender, AccountRole role, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, Set<Long> followings, Set<Long> followers) {
        return new Account(id, loginName, password, profileInfo, privateInfo, gender, role, createdAt, updatedAt, deletedAt, followings, followers);
    }

    public static Account withoutId(String loginName, Password password, ProfileInfo profileInfo, PrivateInfo privateInfo, Gender gender, AccountRole role, LocalDateTime deletedAt, Set<Long> followings, Set<Long> followers) {
        return new Account(null, loginName, password, profileInfo, privateInfo, gender, role, null, null, deletedAt, followings, followers);
    }

    public String getEmail() {
        return this.privateInfo.getEmail();
    }

    public void updateProfileInfo(String nickName, String introduction, Long profileImageId) {

        String updateNickName = nickName != null ? nickName : this.profileInfo.getNickName();
        String updateIntroduction = introduction != null ? introduction : this.profileInfo.getIntroduction();
        Long updateProfileImageId = profileImageId != null ? profileImageId : this.profileInfo.getProfileImageId();

        this.profileInfo = new ProfileInfo(updateNickName, updateIntroduction, updateProfileImageId);
    }

    public void updatePrivateInfo(String name, String phone, String email) {

        String updateName = name != null ? name : this.privateInfo.getName();
        String updatePhone = phone != null ? phone : this.privateInfo.getPhone();
        String updateEmail = email != null ? email : this.privateInfo.getEmail();

        this.privateInfo = new PrivateInfo(updateName, updatePhone, updateEmail);
    }

    public boolean isFollowing(Long targetId) {
        if (targetId == null) return false;
        if (this.followings == null || this.followings.isEmpty()) return false;

        return this.followings.contains(targetId);
    }

    public void addFollowing(Long targetId) {
        if (targetId == null) return;
        if (this.followings == null) this.followings = new HashSet<>();
        this.followings.add(targetId);
    }

    public void removeFollowing(Long targetId) {
        if (targetId == null) return;
        if (this.followings == null || this.followings.isEmpty()) return;
        this.followings.remove(targetId);
    }

    public void addFollower(Long followerId) {
        if (followerId == null) return;
        if (this.followers == null) this.followers = new HashSet<>();
        this.followers.add(followerId);
    }

    public void removeFollower(Long followerId) {
        if (followerId == null) return;
        if (this.followers == null || this.followers.isEmpty()) return;
        this.followers.remove(followerId);
    }

    public void changePassword(String inputPassword) {
        if(inputPassword == null) return;
        this.password = new Password(inputPassword);
    }
}
