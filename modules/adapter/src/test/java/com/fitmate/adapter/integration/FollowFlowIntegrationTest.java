package com.fitmate.adapter.integration;

import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.port.in.account.command.AccountJoinCommand;
import com.fitmate.port.in.account.usecase.AccountProfileUseCasePort;
import com.fitmate.port.out.follow.FollowDetailResponse;
import com.fitmate.adapter.out.persistence.jpa.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[통합] 팔로우 플로우")
class FollowFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private AccountProfileUseCasePort accountUseCase;

    @Autowired
    private AccountRepository accountRepository;

    private Long userAId;
    private Long userBId;

    @BeforeEach
    void setUp() throws Exception {
        accountUseCase.join(new AccountJoinCommand(
                "followA", "!Passw0rd!", "팔로워A", "소개",
                "김팔로", "01099990006", "followA@test.com",
                LocalDate.of(1995, 1, 1), AccountRole.USER, Gender.MALE
        ));
        accountUseCase.join(new AccountJoinCommand(
                "followB", "!Passw0rd!", "팔로워B", "소개",
                "이팔로", "01099990007", "followB@test.com",
                LocalDate.of(2000, 1, 1), AccountRole.USER, Gender.FEMALE
        ));
        userAId = accountRepository.getByLoginName("followA").getId();
        userBId = accountRepository.getByLoginName("followB").getId();
    }

    @Test
    @DisplayName("팔로우 → 팔로잉 목록에 반영")
    void followAndCheckFollowingList() {
        accountUseCase.followOrCancel(userAId, userBId);

        List<FollowDetailResponse> followings = accountUseCase.getFollowingList(userAId);
        assertThat(followings).hasSize(1);
    }

    @Test
    @DisplayName("팔로우 → 팔로워 목록에 반영")
    void followAndCheckFollowerList() {
        accountUseCase.followOrCancel(userAId, userBId);

        List<FollowDetailResponse> followers = accountUseCase.getFollowerList(userBId);
        assertThat(followers).hasSize(1);
    }
}
