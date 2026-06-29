package com.fitmate.adapter.in.web.mate.controller;

import com.fitmate.adapter.in.web.BaseControllerTest;
import com.fitmate.adapter.in.web.mate.dto.MateCancelRequest;
import com.fitmate.adapter.in.web.mate.mapper.MateWebAdapterMapper;
import com.fitmate.adapter.in.web.mate.proxy.MateApplyRetryProxy;
import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.port.in.mate.usecase.MateApplyUseCasePort;
import com.fitmate.port.out.mate.dto.MateQuestionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("DELETE /api/mates/{mateId}/requests/application")
    class CancelMateApply {

        @Test
        @DisplayName("메이트 신청 취소 — 204 No Content")
        void cancelMateApplySuccess() {
            MateRequestController controller = new MateRequestController(
                    mateApplyUseCasePort,
                    mateApplyRetryProxy,
                    mateWebAdapterMapper
            );

            ResponseEntity<?> response = controller.cancelMateApply(
                    10L,
                    new MateCancelRequest(null),
                    new AuthDetails(createTestAccountEntity())
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(response.getBody()).isNull();
        }
    }

    @Nested
    @DisplayName("GET /api/mates/{mateId}/requests/question")
    class GetQuestion {

        @Test
        @DisplayName("신청 질문 조회 — 200 OK + 응답 구조 검증")
        void getQuestionSuccess() throws Exception {
            MateQuestionResponse response = new MateQuestionResponse(1L, "작성자", "어떤 운동 좋아하세요?");
            given(mateApplyUseCasePort.readQuestion(10L)).willReturn(response);

            mockMvc.perform(get("/api/mates/10/requests/question"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.comeQuestion").value("어떤 운동 좋아하세요?"))
                    .andExpect(jsonPath("$.writerName").value("작성자"))
                    .andExpect(jsonPath("$.profileImageId").value(1));
        }
    }
}
