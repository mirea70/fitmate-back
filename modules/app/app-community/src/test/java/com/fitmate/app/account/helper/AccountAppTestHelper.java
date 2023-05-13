package com.fitmate.app.account.helper;

import com.fitmate.app.account.dto.AccountDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.account.entity.vo.Password;
import com.fitmate.domain.account.entity.vo.PrivateInfo;
import com.fitmate.domain.account.entity.vo.ProfileInfo;
import org.springframework.stereotype.Component;

@Component
public class AccountAppTestHelper {
    public AccountDto.JoinRequest getTestAccountJoinRequest() {
        PrivateInfo privateInfo = PrivateInfo.builder()
                .name("미이수")
                .email("abc@naver.com")
                .phone("01011112222")
                .build();
        ProfileInfo profileInfo = ProfileInfo.builder()
                .nickName("닉네임2")
                .build();

        return AccountDto.JoinRequest.builder()
                .loginName("abc2")
                .password("123456aB#")
                .privateInfo(privateInfo)
                .profileInfo(profileInfo)
                .role(AccountRole.USER)
                .gender(Gender.MAIL)
                .build();
    }

    public Account getTestAccount() {
        PrivateInfo privateInfo = PrivateInfo.builder()
                .name("미이수")
                .email("abc@naver.com")
                .phone("01011112222")
                .build();
        ProfileInfo profileInfo = ProfileInfo.builder()
                .nickName("닉네임2")
                .build();

        return Account.builder()
                .loginName("abc2")
                .password(Password.builder().value("123456aB#").build())
                .privateInfo(privateInfo)
                .profileInfo(profileInfo)
                .role(AccountRole.USER)
                .gender(Gender.MAIL)
                .build();
    }

    public AccountDto.JoinResponse getTestJoinResponse() {
        PrivateInfo privateInfo = PrivateInfo.builder()
                .name("미이수")
                .email("abc@naver.com")
                .phone("01011112222")
                .build();
        ProfileInfo profileInfo = ProfileInfo.builder()
                .nickName("닉네임2")
                .build();
        return AccountDto.JoinResponse.builder()
                .loginName("abc")
                .password("123456aB#")
                .privateInfo(privateInfo)
                .profileInfo(profileInfo)
                .role(AccountRole.USER)
                .gender(Gender.MAIL)
                .build();
    }
}
