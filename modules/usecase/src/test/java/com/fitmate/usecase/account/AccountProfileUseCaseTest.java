package com.fitmate.usecase.account;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.AccountId;
import com.fitmate.domain.account.Password;
import com.fitmate.domain.account.PrivateInfo;
import com.fitmate.domain.account.ProfileInfo;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.error.exceptions.DuplicatedException;
import com.fitmate.port.in.account.command.AccountJoinCommand;
import com.fitmate.port.out.account.AccountProfileResponse;
import com.fitmate.port.out.account.LoadAccountPort;
import com.fitmate.port.out.common.Loaded;
import com.fitmate.port.out.file.LoadAttachFilePort;
import com.fitmate.port.out.follow.LoadFollowPort;
import com.fitmate.port.out.mate.LoadMateRequestPort;
import com.fitmate.port.out.notice.LoadNoticePort;
import com.fitmate.usecase.account.mapper.AccountUseCaseMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountProfileUseCase 단위 테스트")
class AccountProfileUseCaseTest {

    @InjectMocks
    private AccountProfileUseCase accountProfileUseCase;

    @Mock private LoadAccountPort loadAccountPort;
    @Mock private LoadFollowPort loadFollowPort;
    @Mock private LoadAttachFilePort loadAttachFilePort;
    @Mock private LoadNoticePort loadNoticePort;
    @Mock private LoadMateRequestPort loadMateRequestPort;
    @Mock private AccountUseCaseMapper accountUseCaseMapper;
    @Mock private ApplicationEventPublisher eventPublisher;

    private Account createAccount(Long id) {
        return Account.withId(
                new AccountId(id), "user" + id, new Password("pw"),
                new ProfileInfo("닉" + id, "소개", null),
                new PrivateInfo("이름", "010", "e@e.com", LocalDate.of(2000, 1, 1)),
                Gender.MALE, AccountRole.USER,
                LocalDateTime.now(), LocalDateTime.now(), null,
                new HashSet<>(), new HashSet<>()
        );
    }

    @Nested
    @DisplayName("join")
    class Join {

        @Test
        @DisplayName("중복 없으면 회원가입 성공")
        void joinSuccess() {
            AccountJoinCommand command = new AccountJoinCommand(
                    "newUser", "Pw1234!!", "닉네임", "소개",
                    "홍길동", "01099998888", "new@test.com",
                    LocalDate.of(2000, 1, 1), AccountRole.USER, Gender.MALE
            );
            given(loadAccountPort.checkDuplicated(eq(null), any(), any(), any(), any())).willReturn(false);
            Account newAccount = createAccount(null);
            given(accountUseCaseMapper.commandToDomain(command)).willReturn(newAccount);

            accountProfileUseCase.join(command);

            then(loadAccountPort).should().saveAccountEntity(newAccount);
        }

        @Test
        @DisplayName("중복 있으면 DuplicatedException")
        void joinDuplicated() {
            AccountJoinCommand command = new AccountJoinCommand(
                    "existUser", "Pw1234!!", "기존닉", "소개",
                    "홍길동", "01011111111", "exist@test.com",
                    LocalDate.of(2000, 1, 1), AccountRole.USER, Gender.MALE
            );
            given(loadAccountPort.checkDuplicated(eq(null), any(), any(), any(), any())).willReturn(true);

            assertThatThrownBy(() -> accountProfileUseCase.join(command))
                    .isInstanceOf(DuplicatedException.class);
        }
    }

    @Nested
    @DisplayName("findAccount")
    class FindAccount {

        @Test
        @DisplayName("계정 조회 성공")
        void findAccountSuccess() {
            Account account = createAccount(1L);
            given(loadAccountPort.loadAccountEntity(any(AccountId.class))).willReturn(account);

            AccountProfileResponse response = new AccountProfileResponse(
                    1L, "user1", "닉1", "소개", null,
                    "이름", "010", "e@e.com",
                    LocalDate.of(2000, 1, 1), "USER", "MALE",
                    Set.of(), Set.of()
            );
            given(accountUseCaseMapper.domainToResponse(account)).willReturn(response);

            AccountProfileResponse result = accountProfileUseCase.findAccount(1L);

            assertThat(result.getAccountId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("followOrCancel")
    class FollowOrCancel {

        @Test
        @DisplayName("팔로우하지 않은 상태이면 팔로우 추가")
        void follow() {
            Account from = createAccount(1L);
            Account target = createAccount(2L);
            given(loadAccountPort.loadAccountEntity(any(AccountId.class)))
                    .willReturn(from, target);

            accountProfileUseCase.followOrCancel(1L, 2L);

            assertThat(from.isFollowing(2L)).isTrue();
            then(loadFollowPort).should().saveFollowEntity(from, target);
        }

        @Test
        @DisplayName("이미 팔로우 중이면 팔로우 취소")
        void cancelFollow() {
            Account from = createAccount(1L);
            from.addFollowing(2L);
            Account target = createAccount(2L);
            target.addFollower(1L);
            given(loadAccountPort.loadAccountEntity(any(AccountId.class)))
                    .willReturn(from, target);

            accountProfileUseCase.followOrCancel(1L, 2L);

            assertThat(from.isFollowing(2L)).isFalse();
            then(loadFollowPort).should().deleteFollowEntity(1L, 2L);
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("삭제 시 이벤트 발행 후 삭제 실행")
        void delete() {
            accountProfileUseCase.delete(1L);

            then(eventPublisher).should().publishEvent(any(Object.class));
            then(loadAccountPort).should().deleteAccountEntity(any(AccountId.class));
        }
    }

    @Test
    @DisplayName("checkDuplicatedLoginName — 중복이면 예외")
    void checkDuplicatedLoginName() {
        given(loadAccountPort.checkDuplicatedLoginName("existUser")).willReturn(true);

        assertThatThrownBy(() -> accountProfileUseCase.checkDuplicatedLoginName("existUser"))
                .isInstanceOf(DuplicatedException.class);
    }

    @Test
    @DisplayName("checkDuplicatedPhone — 중복 아니면 통과")
    void checkDuplicatedPhone() {
        given(loadAccountPort.checkDuplicatedPhone("01012345678")).willReturn(false);

        accountProfileUseCase.checkDuplicatedPhone("01012345678"); // no exception
    }
}
