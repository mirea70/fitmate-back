package com.fitmate.adapter.integration;

import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.error.exceptions.DuplicatedException;
import com.fitmate.port.in.account.command.AccountJoinCommand;
import com.fitmate.port.in.account.usecase.AccountProfileUseCasePort;
import com.fitmate.port.out.account.AccountProfileResponse;
import com.fitmate.port.out.follow.FollowDetailResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("[통합] 회원 기능 플로우")
class AccountFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private AccountProfileUseCasePort accountUseCase;

    private Long userAId;
    private Long userBId;

    @BeforeEach
    void setUp() throws Exception {
        accountUseCase.join(new AccountJoinCommand(
                "userA", "!Passw0rd!", "유저A", "소개A",
                "홍길동", "01011111111", "a@test.com",
                LocalDate.of(1995, 3, 15), AccountRole.USER, Gender.MALE
        ));
        accountUseCase.join(new AccountJoinCommand(
                "userB", "!Passw0rd!", "유저B", "소개B",
                "김철수", "01022222222", "b@test.com",
                LocalDate.of(2000, 7, 20), AccountRole.USER, Gender.FEMALE
        ));

        // 가입 후 ID 추출 (loginName으로 조회하여 확인)
        accountUseCase.checkDuplicatedLoginName("nonexist"); // 정상 통과 확인
        // ID는 findAccount로 가져올 수 없으므로 (ID를 모르니까), DB에서 직접 조회
        // 대신 checkDuplicatedLoginName으로 가입 여부 확인
    }

    @Nested
    @DisplayName("회원가입")
    class Join {

        @Test
        @DisplayName("가입 후 로그인명 중복 체크 시 예외 발생")
        void joinAndCheckDuplicate() {
            assertThatThrownBy(() -> accountUseCase.checkDuplicatedLoginName("userA"))
                    .isInstanceOf(DuplicatedException.class);
        }

        @Test
        @DisplayName("전화번호 중복 체크")
        void phoneAlreadyExists() {
            assertThatThrownBy(() -> accountUseCase.checkDuplicatedPhone("01011111111"))
                    .isInstanceOf(DuplicatedException.class);
        }

        @Test
        @DisplayName("중복 닉네임으로 가입하면 DuplicatedException")
        void duplicateNickName() throws Exception {
            assertThatThrownBy(() -> accountUseCase.join(new AccountJoinCommand(
                    "userC", "!Passw0rd!", "유저A", "소개",
                    "이영희", "01033333333", "c@test.com",
                    LocalDate.of(1998, 1, 1), AccountRole.USER, Gender.FEMALE
            ))).isInstanceOf(DuplicatedException.class);
        }
    }
}
