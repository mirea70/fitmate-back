package com.fitmate.domain.account;

import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Account 도메인 테스트")
class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.withId(
                new AccountId(1L),
                "testUser",
                new Password("encodedPw"),
                new ProfileInfo("닉네임", "소개글", 100L),
                new PrivateInfo("홍길동", "01012345678", "test@test.com", LocalDate.of(2000, 1, 1)),
                Gender.MALE,
                AccountRole.USER,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                new HashSet<>(Set.of(2L, 3L)),
                new HashSet<>(Set.of(4L))
        );
    }

    @Nested
    @DisplayName("프로필 정보 수정")
    class UpdateProfileInfo {

        @Test
        @DisplayName("닉네임만 수정하면 나머지는 기존 값 유지")
        void updateNickNameOnly() {
            account.updateProfileInfo("새닉네임", null, null);

            assertThat(account.getProfileInfo().getNickName()).isEqualTo("새닉네임");
            assertThat(account.getProfileInfo().getIntroduction()).isEqualTo("소개글");
            assertThat(account.getProfileInfo().getProfileImageId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("모든 프로필 정보를 수정")
        void updateAll() {
            account.updateProfileInfo("새닉네임", "새소개", 200L);

            assertThat(account.getProfileInfo().getNickName()).isEqualTo("새닉네임");
            assertThat(account.getProfileInfo().getIntroduction()).isEqualTo("새소개");
            assertThat(account.getProfileInfo().getProfileImageId()).isEqualTo(200L);
        }
    }

    @Nested
    @DisplayName("개인 정보 수정")
    class UpdatePrivateInfo {

        @Test
        @DisplayName("이메일만 수정하면 나머지는 기존 값 유지")
        void updateEmailOnly() {
            account.updatePrivateInfo(null, null, "new@test.com");

            assertThat(account.getPrivateInfo().getEmail()).isEqualTo("new@test.com");
            assertThat(account.getPrivateInfo().getName()).isEqualTo("홍길동");
            assertThat(account.getPrivateInfo().getPhone()).isEqualTo("01012345678");
        }
    }

    @Nested
    @DisplayName("나이 계산")
    class GetAge {

        @Test
        @DisplayName("생년월일이 있으면 현재 나이를 반환")
        void returnsAge() {
            int age = account.getAge();
            int expectedAge = LocalDate.now().getYear() - 2000;
            assertThat(age).isBetween(expectedAge - 1, expectedAge);
        }

        @Test
        @DisplayName("생년월일이 null이면 0 반환")
        void returnsZeroWhenNoBirthDate() {
            Account noBirth = Account.withId(
                    new AccountId(2L), "user2", new Password("pw"),
                    new ProfileInfo("닉", "소개", null),
                    new PrivateInfo("이름", "010", "e@e.com", null),
                    Gender.MALE, AccountRole.USER,
                    LocalDateTime.now(), LocalDateTime.now(), null, null, null
            );
            assertThat(noBirth.getAge()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("팔로우")
    class Following {

        @Test
        @DisplayName("팔로잉 여부를 확인")
        void isFollowing() {
            assertThat(account.isFollowing(2L)).isTrue();
            assertThat(account.isFollowing(99L)).isFalse();
            assertThat(account.isFollowing(null)).isFalse();
        }

        @Test
        @DisplayName("팔로잉 추가")
        void addFollowing() {
            account.addFollowing(10L);
            assertThat(account.isFollowing(10L)).isTrue();
        }

        @Test
        @DisplayName("null 팔로잉 추가 시 무시")
        void addNullFollowing() {
            int before = account.getFollowings().size();
            account.addFollowing(null);
            assertThat(account.getFollowings()).hasSize(before);
        }

        @Test
        @DisplayName("팔로잉 제거")
        void removeFollowing() {
            account.removeFollowing(2L);
            assertThat(account.isFollowing(2L)).isFalse();
        }

        @Test
        @DisplayName("팔로워 추가/제거")
        void follower() {
            account.addFollower(10L);
            assertThat(account.getFollowers()).contains(10L);

            account.removeFollower(10L);
            assertThat(account.getFollowers()).doesNotContain(10L);
        }

        @Test
        @DisplayName("followings가 null인 계정에 팔로잉 추가")
        void addFollowingWhenNull() {
            Account fresh = Account.withId(
                    new AccountId(3L), "user3", new Password("pw"),
                    new ProfileInfo("닉", null, null),
                    new PrivateInfo("이름", "010", "e@e.com", null),
                    Gender.MALE, AccountRole.USER,
                    LocalDateTime.now(), LocalDateTime.now(), null, null, null
            );
            fresh.addFollowing(5L);
            assertThat(fresh.isFollowing(5L)).isTrue();
        }
    }

    @Nested
    @DisplayName("비밀번호 변경")
    class ChangePassword {

        @Test
        @DisplayName("비밀번호 변경 성공")
        void changePassword() {
            account.changePassword("newEncodedPw");
            assertThat(account.getPassword().getValue()).isEqualTo("newEncodedPw");
        }

        @Test
        @DisplayName("null 입력 시 변경 안됨")
        void nullPassword() {
            account.changePassword(null);
            assertThat(account.getPassword().getValue()).isEqualTo("encodedPw");
        }
    }

    @Test
    @DisplayName("getEmail은 privateInfo의 이메일을 반환")
    void getEmail() {
        assertThat(account.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("withoutId로 생성하면 id, createdAt, updatedAt이 null")
    void withoutId() {
        Account account = Account.withoutId(
                "login", new Password("pw"),
                new ProfileInfo("닉", null, null),
                new PrivateInfo("이름", "010", "e@e.com", null),
                Gender.MALE, AccountRole.USER, null, null, null
        );
        assertThat(account.getId()).isNull();
        assertThat(account.getCreatedAt()).isNull();
    }
}
