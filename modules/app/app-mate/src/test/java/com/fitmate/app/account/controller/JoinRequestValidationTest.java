package com.fitmate.app.account.controller;

import com.fitmate.app.mate.account.controller.AccountController;
import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.account.helper.AccountAppTestHelper;
import com.fitmate.app.account.helper.AccountMockMvcHelper;
import com.fitmate.domain.account.vo.PrivateInfo;
import com.fitmate.domain.account.vo.ProfileInfo;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class JoinRequestValidationTest {
    @InjectMocks
    private AccountController target;
    private AccountAppTestHelper accountAppTestHelper;
    private AccountMockMvcHelper accountMockMvcHelper;
    private final String url = "/api/accounts/join";

    @BeforeEach
    public void init() {
        accountAppTestHelper = new AccountAppTestHelper();
        accountMockMvcHelper = new AccountMockMvcHelper(target);
    }

    @Test
    public void mockMvc_주입테스트 () throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(target).build();
        // then
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("invalidJoinParentParameter")
    public void 회원가입실패_잘못된파라미터_부모 (final String loginName, final String password
                            , final ProfileInfo profileInfo, final PrivateInfo privateInfo,
                                   final AccountRole role, final Gender gender) throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = AccountDto.JoinRequest.builder()
                .loginName(loginName)
                .password(password)
                .profileInfo(profileInfo)
                .privateInfo(privateInfo)
                .role(role)
                .gender(gender)
                .build();
        // when
        ResultActions resultActions = accountMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidJoinParentParameter() {
        ProfileInfo profileInfo = ProfileInfo.builder()
                .nickName("닉네임2")
                .build();
        PrivateInfo privateInfo = PrivateInfo.builder()
                .name("미이수")
                .email("abc@naver.com")
                .phone("01011112222")
                .build();

        return Stream.of(
                // loginName
                Arguments.of(null, "123456aB#", profileInfo, privateInfo, AccountRole.USER, Gender.MAIL),
                // password
                Arguments.of("abc", "aB34%", profileInfo, privateInfo, AccountRole.USER, Gender.MAIL),
                Arguments.of("abc", "aBc1234567", profileInfo, privateInfo, AccountRole.USER, Gender.MAIL),
                Arguments.of("abc", "1234567%", profileInfo, privateInfo, AccountRole.USER, Gender.MAIL),
                Arguments.of("abc", "1234567a%", profileInfo, privateInfo, AccountRole.USER, Gender.MAIL),
                Arguments.of("abc", "1234567A%", profileInfo, privateInfo, AccountRole.USER, Gender.MAIL),
                // profileInfo
                Arguments.of("abc", "123456aB#", null, privateInfo, AccountRole.USER, Gender.MAIL),
                // privateInfo
                Arguments.of("abc", "123456aB#", profileInfo, null, AccountRole.USER, Gender.MAIL),
                // role
                Arguments.of("abc", "123456aB#", profileInfo, privateInfo, null, Gender.MAIL),
                // gender
                Arguments.of("abc", "123456aB#", profileInfo, privateInfo, AccountRole.USER, null)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidJoinChildParameter")
    public void 회원가입실패_잘못된파라미터_자식 (final String name, final String email, final String phone,
                                   final String nickName, final String introduction) throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();

        PrivateInfo privateInfo = PrivateInfo.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .build();
        ProfileInfo profileInfo = ProfileInfo.builder()
                .nickName(nickName)
                .introduction(introduction)
                .build();
        joinRequest.setPrivateInfo(privateInfo);
        joinRequest.setProfileInfo(profileInfo);
        // when
        ResultActions resultActions = accountMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidJoinChildParameter() {
        final String testIntroduction51 = "012345678901234567890123456789012345678901234567890";

        return Stream.of(
                // name
                Arguments.of(null, "test@naver.com", "01032341323", "마이","소개2"),
                Arguments.of("아", "test@naver.com", "01032341323", "마이","소개2"),
                Arguments.of("아리아마타불", "test@naver.com", "01032341323", "마이","소개2"),
                Arguments.of("ab가", "test@naver.com", "01032341323", "마이","소개2"),
                // email
                Arguments.of("홍길동", null, "01032341323", "마이","소개2"),
                Arguments.of("홍길동", "abcx", "01032341323", "마이","소개2"),
                // phone
                Arguments.of("홍길동", "test@naver.com", null, "마이","소개2"),
                Arguments.of("홍길동", "test@naver.com", "010134", "마이","소개2"),
                // nickName
                Arguments.of("홍길동", "test@naver.com", "01032341323", null,"소개2"),
                Arguments.of("홍길동", "test@naver.com", "010323랴1323", "하","소개2"),
                Arguments.of("홍길동", "test@naver.com", "01032341323", "abc12345678910", "소개2"),
                Arguments.of("홍길동", "test@naver.com", "01032341323", "abc12%", "소개2"),
                Arguments.of("홍길동", "test@naver.com", "01032341323", "마이", testIntroduction51)
        );
    }
}
