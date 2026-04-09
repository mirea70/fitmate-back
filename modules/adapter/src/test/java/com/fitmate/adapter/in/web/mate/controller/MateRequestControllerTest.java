package com.fitmate.adapter.in.web.mate.controller;

import com.fitmate.adapter.in.web.BaseControllerTest;
import com.fitmate.adapter.in.web.mate.mapper.MateWebAdapterMapper;
import com.fitmate.adapter.in.web.mate.proxy.MateApplyRetryProxy;
import com.fitmate.port.in.mate.usecase.MateApplyUseCasePort;
import com.fitmate.port.out.mate.dto.MateQuestionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MateRequestController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@DisplayName("MateRequestController 테스트")
class MateRequestControllerTest extends BaseControllerTest {

    @MockBean
    private MateApplyUseCasePort mateApplyUseCasePort;

    @MockBean
    private MateApplyRetryProxy mateApplyRetryProxy;

    @MockBean
    private MateWebAdapterMapper mateWebAdapterMapper;

    @Nested
    @DisplayName("GET /api/mate/request/{mateId}/question")
    class GetQuestion {

        @Test
        @DisplayName("신청 질문 조회 — 200 OK + 응답 구조 검증")
        void getQuestionSuccess() throws Exception {
            MateQuestionResponse response = new MateQuestionResponse(1L, "작성자", "어떤 운동 좋아하세요?");
            given(mateApplyUseCasePort.readQuestion(10L)).willReturn(response);

            mockMvc.perform(get("/api/mate/request/10/question"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.comeQuestion").value("어떤 운동 좋아하세요?"))
                    .andExpect(jsonPath("$.writerName").value("작성자"))
                    .andExpect(jsonPath("$.profileImageId").value(1));
        }
    }
}
