package com.fitmate.app.account.controller;

import com.fitmate.app.account.dto.AccountDto;
import com.fitmate.app.account.helper.AccountAppTestHelper;
import com.fitmate.app.account.helper.JoinMockMvcHelper;
import com.fitmate.domain.account.entity.vo.PrivateInfo;
import com.fitmate.domain.account.entity.vo.ProfileInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class JoinRequestValidationTest {
    @InjectMocks
    private AccountController target;
    private AccountAppTestHelper accountAppTestHelper;
    private JoinMockMvcHelper joinMockMvcHelper;
    private final String url = "/api/accounts/join";

    @BeforeEach
    public void init() {
        accountAppTestHelper = new AccountAppTestHelper();
        joinMockMvcHelper = new JoinMockMvcHelper(target);
    }

    @Test
    public void mockMvc_주입테스트 () throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(target).build();
        // then
        assertThat(target).isNotNull();
        assertThat(mockMvc).isNotNull();
    }
    
    @Test
    public void 회원가입실패_loginName이Null () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        joinRequest.setLoginName(null);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_password가Null () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        joinRequest.setPassword(null);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }
    
    @Test
    public void 회원가입실패_password가_8자리미만 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        joinRequest.setPassword("1234567");
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_password_특수문자X () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        joinRequest.setPassword("abc1234567");
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    public void 회원가입실패_password_영문자X () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        joinRequest.setPassword("1234567%");
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_password_영대문자X () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        joinRequest.setPassword("1234567a%");
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    public void 회원가입실패_password_영소문자X () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        joinRequest.setPassword("1234567A%b");
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }


    @Test
    public void 회원가입실패_privateInfo가Null () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        joinRequest.setPrivateInfo(null);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_privateInfo_name이Null () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        PrivateInfo newPrivateInfo = PrivateInfo.builder()
                .email("test@naver.com")
                .phone("01032341323")
                .build();

        joinRequest.setPrivateInfo(newPrivateInfo);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_privateInfo_name이2자리미만 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        PrivateInfo newPrivateInfo = PrivateInfo.builder()
                .name("아")
                .email("test@naver.com")
                .phone("01032341323")
                .build();

        joinRequest.setPrivateInfo(newPrivateInfo);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_privateInfo_name이5자리초과 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        PrivateInfo newPrivateInfo = PrivateInfo.builder()
                .name("아미아타불만세")
                .email("test@naver.com")
                .phone("01032341323")
                .build();

        joinRequest.setPrivateInfo(newPrivateInfo);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_privateInfo_name이한글X () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        PrivateInfo newPrivateInfo = PrivateInfo.builder()
                .name("ab가")
                .email("test@naver.com")
                .phone("01032341323")
                .build();

        joinRequest.setPrivateInfo(newPrivateInfo);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_privateInfo_email이Null () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        PrivateInfo newPrivateInfo = PrivateInfo.builder()
                .name("홍길동")
                .phone("01032341323")
                .build();

        joinRequest.setPrivateInfo(newPrivateInfo);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_privateInfo_email형식X () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        PrivateInfo newPrivateInfo = PrivateInfo.builder()
                .name("홍길동")
                .email("abcx")
                .phone("01032341323")
                .build();

        joinRequest.setPrivateInfo(newPrivateInfo);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_privateInfo_phone이Null () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        PrivateInfo newPrivateInfo = PrivateInfo.builder()
                .name("홍길동")
                .email("test@naver.com")
                .build();

        joinRequest.setPrivateInfo(newPrivateInfo);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_privateInfo_phone형식검증 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        PrivateInfo newPrivateInfo = PrivateInfo.builder()
                .name("홍길동")
                .phone("010134")
                .email("test@naver.com")
                .build();

        joinRequest.setPrivateInfo(newPrivateInfo);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_profileInfo가Null () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        joinRequest.setProfileInfo(null);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    public void 회원가입실패_profileInfo_nickName이Null () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        ProfileInfo newProfileInfo = ProfileInfo.builder().build();

        joinRequest.setProfileInfo(newProfileInfo);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_profileInfo_nickName이2자리미만 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        ProfileInfo newProfileInfo = ProfileInfo.builder()
                .nickName("하")
                .build();

        joinRequest.setProfileInfo(newProfileInfo);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_profileInfo_nickName이10자리초과 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        ProfileInfo newProfileInfo = ProfileInfo.builder()
                .nickName("abc12345678910")
                .build();

        joinRequest.setProfileInfo(newProfileInfo);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_profileInfo_nickName이특수문자포함 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        ProfileInfo newProfileInfo = ProfileInfo.builder()
                .nickName("abc12%")
                .build();

        joinRequest.setProfileInfo(newProfileInfo);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_profileInfo_introduction_50자초과 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        ProfileInfo newProfileInfo = ProfileInfo.builder()
                .nickName("마이")
                // 51자
                .introduction("012345678901234567890123456789012345678901234567890")
                .build();

        joinRequest.setProfileInfo(newProfileInfo);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_role이Null () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        joinRequest.setRole(null);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }
    @Test
    public void 회원가입실패_gender이Null () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        joinRequest.setGender(null);
        // when
        final ResultActions resultActions = joinMockMvcHelper.submitPost(joinRequest, url);
        // then
        resultActions.andExpect(status().isBadRequest());
    }
}
