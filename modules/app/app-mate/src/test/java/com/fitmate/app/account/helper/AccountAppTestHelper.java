package com.fitmate.app.account.helper;

import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.account.vo.Password;
import com.fitmate.domain.account.vo.PrivateInfo;
import com.fitmate.domain.account.vo.ProfileInfo;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

@Component
public class AccountAppTestHelper {
    private final FileTestHelper fileTestHelper = new FileTestHelper();


    public AccountDto.JoinRequest getTestAccountJoinRequest() throws Exception {
        PrivateInfo privateInfo = PrivateInfo.builder()
                .name("미이수")
                .email("abc@naver.com")
                .phone("01011112222")
                .build();
        ProfileInfo profileInfo = ProfileInfo.builder()
                .nickName("닉네임2")
                .build();

        MockMultipartFile multipartFile = fileTestHelper.getMockMultipartFile("image");

        return AccountDto.JoinRequest.builder()
                .loginName("abc2")
                .password("123456aB#")
                .privateInfo(privateInfo)
                .profileInfo(profileInfo)
                .role(AccountRole.USER)
                .gender(Gender.MAIL)
                .profileImage(multipartFile)
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
                .profileImageId(1L)
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
