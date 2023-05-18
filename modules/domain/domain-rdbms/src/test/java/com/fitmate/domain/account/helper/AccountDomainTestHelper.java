package com.fitmate.domain.account.helper;

import com.fitmate.domain.account.dto.AccountDuplicateCheckDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.account.vo.Password;
import com.fitmate.domain.account.vo.PrivateInfo;
import com.fitmate.domain.account.vo.ProfileInfo;
import org.springframework.stereotype.Component;

@Component
public class AccountDomainTestHelper {
    public Account getTestAccount() {

        final PrivateInfo privateInfo = PrivateInfo.builder()
                .name("홍길동")
                .email("abc@naver.com")
                .phone("01011112222")
                .build();

        final ProfileInfo profileInfo = ProfileInfo.builder()
                .nickName("마이")
                .build();

        return Account.builder()
                .loginName("asd1")
                .password(Password.builder().value("12345678").build())
                .privateInfo(privateInfo)
                .profileInfo(profileInfo)
                .gender(Gender.MAIL)
                .role(AccountRole.USER)
                .build();
    }

    public AccountDuplicateCheckDto getTestDuplicateCheckDto() {
        return AccountDuplicateCheckDto.builder()
                .name("홍길동")
                .email("abc@naver.com")
                .phone("01011112222")
                .nickName("마이")
                .build();
    }
}
