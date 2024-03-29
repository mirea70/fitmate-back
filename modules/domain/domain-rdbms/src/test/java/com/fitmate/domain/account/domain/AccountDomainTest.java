package com.fitmate.domain.account.domain;

import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.helper.AccountDomainTestHelper;
import com.fitmate.domain.account.vo.Password;
import com.fitmate.domain.account.vo.PrivateInfo;
import com.fitmate.domain.account.vo.ProfileInfo;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(AccountDomainTestHelper.class)
public class AccountDomainTest {
    private final AccountDomainTestHelper accountDomainTestHelper = new AccountDomainTestHelper();


    @Test
    public void 회원프로필_수정_테스트() {
        // given
        Account orgAccount = accountDomainTestHelper.getTestAccount();

        final PrivateInfo newPrivateInfo = PrivateInfo.builder()
                .name("[수정]홍길동")
                .email("modify@naver.com")
                .phone("01099998999")
                .build();

        final ProfileInfo newProfileInfo = ProfileInfo.builder()
                .nickName("[수정]마이")
                .introduction("[수정]소개")
                .profileImageId(33L)
                .build();
        // when
        orgAccount.modifyProfile( newPrivateInfo, newProfileInfo);
        // then
        assertThat(orgAccount.getPrivateInfo()).isEqualTo(newPrivateInfo);
        assertThat(orgAccount.getProfileInfo()).isEqualTo(newProfileInfo);
    }
}
