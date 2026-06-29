package com.fitmate.adapter.in.web.mate.controller;

import com.fitmate.adapter.in.web.BaseControllerTest;
import com.fitmate.adapter.in.web.mate.dto.MateCreateRequest;
import com.fitmate.adapter.in.web.mate.mapper.MateWebAdapterMapper;
import com.fitmate.adapter.in.web.security.dto.AuthDetails;
import com.fitmate.adapter.out.persistence.jpa.file.adapter.AttachFilePersistenceAdapter;
import com.fitmate.domain.mate.enums.FitCategory;
import com.fitmate.domain.mate.enums.GatherType;
import com.fitmate.domain.mate.enums.PermitGender;
import com.fitmate.port.in.mate.command.MateCreateCommand;
import com.fitmate.port.in.mate.usecase.MateUseCasePort;
import com.fitmate.port.out.common.SliceResponse;
import com.fitmate.port.out.mate.dto.MateDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MateController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@DisplayName("MateController 테스트")
class MateControllerTest extends BaseControllerTest {

    @MockBean
    private MateUseCasePort mateUseCasePort;

    @MockBean
    private MateWebAdapterMapper mateWebAdapterMapper;

    @MockBean
    private AttachFilePersistenceAdapter filePersistenceAdapter;

    @Nested
    @DisplayName("POST /api/mates")
    class Register {

        @Test
        @DisplayName("메이트 글 작성 — 201 Created")
        void registerSuccess() throws Exception {
            MateCreateRequest request = new MateCreateRequest(
                    FitCategory.FITNESS,
                    "운동 메이트 구함",
                    "같이 운동해요",
                    LocalDateTime.now().plusDays(7),
                    "험블짐",
                    "서울시 용산구",
                    GatherType.AGREE,
                    PermitGender.ALL,
                    50,
                    15,
                    5,
                    Collections.emptyList(),
                    "어떤 운동 좋아하세요?"
            );
            MateCreateCommand command = new MateCreateCommand(
                    FitCategory.FITNESS,
                    "운동 메이트 구함",
                    "같이 운동해요",
                    null,
                    request.getMateAt(),
                    "험블짐",
                    "서울시 용산구",
                    GatherType.AGREE,
                    PermitGender.ALL,
                    50,
                    15,
                    5,
                    Collections.emptyList(),
                    "어떤 운동 좋아하세요?",
                    TEST_ACCOUNT_ID
            );
            given(mateWebAdapterMapper.requestToCommand(any(MateCreateRequest.class), any(), any())).willReturn(command);

            AuthDetails authDetails = new AuthDetails(createTestAccountEntity());
            MateController controller = new MateController(mateUseCasePort, mateWebAdapterMapper, filePersistenceAdapter);

            ResponseEntity<?> response = controller.register(request, null, authDetails);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }
    }

    @Nested
    @DisplayName("GET /api/mates/{mateId}")
    class FindOne {

        @Test
        @DisplayName("메이트 글 단일조회 — 200 OK + 응답 구조 검증")
        void findOneSuccess() throws Exception {
            MateDetailResponse response = new MateDetailResponse(
                    10L, 1L, "작성자닉네임", 1L,
                    FitCategory.FITNESS, "운동 메이트 구함", "같이 해요", Set.of(),
                    LocalDateTime.now().plusDays(7),
                    "험블짐", "서울시 용산구",
                    GatherType.AGREE, PermitGender.ALL, 50, 15, 5,
                    10000, Collections.emptyList(), "질문",
                    Set.of(), Set.of(1L), false
            );
            given(mateUseCasePort.findMate(10L)).willReturn(response);

            mockMvc.perform(get("/api/mates/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(10))
                    .andExpect(jsonPath("$.title").value("운동 메이트 구함"))
                    .andExpect(jsonPath("$.fitCategory").value("FITNESS"))
                    .andExpect(jsonPath("$.permitPeopleCnt").value(5))
                    .andExpect(jsonPath("$.closed").value(false));
        }
    }

    @Nested
    @DisplayName("POST /api/mates/list")
    class FindList {

        @Test
        @DisplayName("메이트 목록 조회 — 200 OK + 페이징 응답 구조 검증")
        void findListSuccess() throws Exception {
            SliceResponse response = new SliceResponse(List.of(), 0, 10, true, true);
            given(mateUseCasePort.findAllMate(any())).willReturn(response);

            String requestBody = """
                    {
                        "page": 0,
                        "size": 10,
                        "sortDir": "DESC",
                        "sortProperty": "createdAt"
                    }
                    """;

            mockMvc.perform(post("/api/mates/list")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.currentPage").value(0))
                    .andExpect(jsonPath("$.size").value(10))
                    .andExpect(jsonPath("$.first").value(true));
        }
    }
}
