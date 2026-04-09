package com.fitmate.adapter.in.web.account.controller;

import com.fitmate.adapter.in.web.BaseControllerTest;
import com.fitmate.adapter.in.web.account.dto.AccountJoinRequest;
import com.fitmate.adapter.in.web.account.mapper.AccountWebAdapterMapper;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.domain.error.exceptions.DuplicatedException;
import com.fitmate.domain.error.results.DuplicatedErrorResult;
import com.fitmate.port.in.account.usecase.AccountProfileUseCasePort;
import com.fitmate.port.out.account.AccountProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AccountController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@DisplayName("AccountController 테스트")
class AccountControllerTest extends BaseControllerTest {

    @MockBean
    private AccountProfileUseCasePort accountProfileUseCasePort;

    @MockBean
    private AccountWebAdapterMapper accountWebAdapterMapper;

    @Nested
    @DisplayName("POST /api/account/join — 회원가입 (토큰 불필요)")
    class JoinTest {

        @Test
        @DisplayName("정상 회원가입 — 200 OK")
        void joinSuccess() throws Exception {
            AccountJoinRequest request = new AccountJoinRequest(
                    "abc2", "!Qqweras33!!", "홍시", "안녕하세요",
                    "홍길동", "01012345678", "abc@naver.com",
                    AccountRole.USER, LocalDate.of(1995, 3, 15), Gender.MALE
            );

            willDoNothing().given(accountProfileUseCasePort).join(any());

            mockMvc.perform(post("/api/account/join")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Content-Type 없이 요청 시 — 415 Unsupported Media Type")
        void joinWithoutContentType() throws Exception {
            mockMvc.perform(post("/api/account/join"))
                    .andExpect(status().isUnsupportedMediaType());
        }
    }

    @Nested
    @DisplayName("GET /api/account/check/loginName — 중복 체크 (토큰 불필요)")
    class CheckLoginName {

        @Test
        @DisplayName("중복 아닌 경우 — 200 OK")
        void notDuplicated() throws Exception {
            willDoNothing().given(accountProfileUseCasePort).checkDuplicatedLoginName("abc2");

            mockMvc.perform(get("/api/account/check/loginName")
                            .param("loginName", "abc2"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("중복인 경우 — 400 Bad Request")
        void duplicated() throws Exception {
            willThrow(new DuplicatedException(DuplicatedErrorResult.DUPLICATED_ACCOUNT_JOIN))
                    .given(accountProfileUseCasePort).checkDuplicatedLoginName("abc2");

            mockMvc.perform(get("/api/account/check/loginName")
                            .param("loginName", "abc2"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/account/check/phone — 중복 체크 (토큰 불필요)")
    class CheckPhone {

        @Test
        @DisplayName("중복 아닌 경우 — 200 OK")
        void notDuplicated() throws Exception {
            willDoNothing().given(accountProfileUseCasePort).checkDuplicatedPhone("01012345678");

            mockMvc.perform(get("/api/account/check/phone")
                            .param("phone", "01012345678"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /api/account/{accountId} — 프로필 조회")
    class FindById {

        @Test
        @DisplayName("특정 유저 프로필 조회 — 200 OK + 응답 구조 검증")
        void findById() throws Exception {
            AccountProfileResponse response = new AccountProfileResponse(
                    2L, "otherUser", "다른유저", "소개", null,
                    "김철수", "01099998888", "other@test.com",
                    LocalDate.of(1998, 5, 20), "USER", "FEMALE",
                    Set.of(), Set.of()
            );
            given(accountProfileUseCasePort.findAccount(2L)).willReturn(response);

            mockMvc.perform(get("/api/account/2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accountId").value(2))
                    .andExpect(jsonPath("$.loginName").value("otherUser"))
                    .andExpect(jsonPath("$.nickName").value("다른유저"))
                    .andExpect(jsonPath("$.gender").value("FEMALE"));
        }
    }

    @Nested
    @DisplayName("DELETE /api/account/{accountId} — 회원 탈퇴")
    class DeleteAccount {

        @Test
        @DisplayName("회원 탈퇴 — 200 OK")
        void deleteSuccess() throws Exception {
            willDoNothing().given(accountProfileUseCasePort).delete(1L);

            mockMvc.perform(delete("/api/account/1"))
                    .andExpect(status().isOk());
        }
    }
}
