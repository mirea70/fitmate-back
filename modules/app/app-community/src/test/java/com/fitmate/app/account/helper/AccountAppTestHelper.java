package com.fitmate.app.account.helper;

import com.fitmate.app.account.dto.AccountDto;
import com.fitmate.domain.account.dto.AccountDuplicateCheckDto;
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
                .password("12345678")
                .privateInfo(privateInfo)
                .profileInfo(profileInfo)
                .role(AccountRole.USER)
                .gender(Gender.MAIL)
                .build();
    }

    public Account getAccountByRequestDto(AccountDto.JoinRequest joinRequest) {

        return Account.builder()
                .loginName(joinRequest.getLoginName())
                .password(Password.builder().value(joinRequest.getPassword()).build())
                .privateInfo(joinRequest.getPrivateInfo())
                .profileInfo(joinRequest.getProfileInfo())
                .gender(joinRequest.getGender())
                .role(joinRequest.getRole())
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
                .password(Password.builder().value("12345678").build())
                .privateInfo(privateInfo)
                .profileInfo(profileInfo)
                .role(AccountRole.USER)
                .gender(Gender.MAIL)
                .build();
    }

    public AccountDuplicateCheckDto getTestDuplicateCheckDto(AccountDto.JoinRequest joinRequest) {
        return AccountDuplicateCheckDto.builder()
                .name(joinRequest.getPrivateInfo().getName())
                .email(joinRequest.getPrivateInfo().getEmail())
                .phone(joinRequest.getPrivateInfo().getPhone())
                .nickName(joinRequest.getProfileInfo().getNickName())
                .build();
    }
}
