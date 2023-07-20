package com.fitmate.app.mating.controller;

import com.fitmate.app.account.helper.FileTestHelper;
import com.fitmate.app.mate.exceptions.GlobalExceptionHandler;
import com.fitmate.app.mate.mating.controller.MatingController;
import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.service.MatingRegisterService;
import com.fitmate.app.mating.helper.MatingAppTestHelper;
import com.fitmate.app.mating.helper.MatingMockMvcHelper;
import com.fitmate.config.GsonUtil;
import com.fitmate.domain.mating.mate.domain.entity.Mating;
import com.fitmate.domain.mating.mate.domain.repository.MatingReadRepository;
import com.fitmate.domain.mating.mate.dto.MatingReadResponseDto;
import com.fitmate.domain.mating.mate.service.MatingService;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.result.NotFoundErrorResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MatingControllerTest {
    @InjectMocks
    private MatingController target;
    @Mock
    private MatingRegisterService matingRegisterService;
    @Mock
    private MatingService matingService;
    @Mock
    private MatingReadRepository matingReadRepository;

    private MatingAppTestHelper matingAppTestHelper;

    private MatingMockMvcHelper matingMockMvcHelper;

    private FileTestHelper fileTestHelper;

    private Gson gson;

    @BeforeEach
    public void init() {
        matingAppTestHelper = new MatingAppTestHelper();
        matingMockMvcHelper = new MatingMockMvcHelper(target, new GlobalExceptionHandler());
        fileTestHelper = new FileTestHelper();
        gson = GsonUtil.buildGson();
    }

    /**
     * LocalDateTime 관련 에러 해결 필요
     */
    @Test
    public void 메이팅찾기글_등록() throws Exception {
        // given
        final String url = "/api/mating";
        MatingDto.Create requestDto = matingAppTestHelper.getTestRequest();
        MockMultipartFile mockMultipartFile = fileTestHelper.getMockMultipartFile("image");
        MatingDto.Response responseDto = matingAppTestHelper.getTestResponse();
        doReturn(responseDto).when(matingRegisterService).register(any(MatingDto.Create.class));
        // when
        final ResultActions resultActions = matingMockMvcHelper.submitMultiPart(requestDto, url, mockMultipartFile);
        // then
        final MatingDto.Response resultResponse = gson.fromJson(resultActions.andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), MatingDto.Response.class);

        assertThat(resultResponse).isNotNull();
        assertThat(resultResponse.getFitPlace()).isEqualTo(requestDto.getFitPlace());
        assertThat(resultResponse.getTitle()).isEqualTo(requestDto.getTitle());
        resultActions.andExpect(status().isCreated());

    }

    @Test
    public void 메이팅찾기글_조회_실패_서비스에서_NotFoundException() throws Exception {
        // given
        String url = "/api/mating/1";
        doThrow(new NotFoundException(NotFoundErrorResult.NOT_FOUND_MATING_DATA)).when(matingService).validateFindById(anyLong());
        // when
        ResultActions resultActions = matingMockMvcHelper.submitGet(url);
        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void 메이팅찾기글_조회_성공() throws Exception {
        // given
        String url = "/api/mating/1";
        Mating findMating = matingAppTestHelper.getTestMating();
        doReturn(findMating).when(matingService).validateFindById(anyLong());
        // when
        ResultActions resultActions = matingMockMvcHelper.submitGet(url);
        MatingDto.Response resultResponse = gson.fromJson(resultActions.andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8), MatingDto.Response.class);
        // then
        resultActions.andExpect(status().isOk());
        assertThat(resultResponse.getId()).isEqualTo(1L);
    }

    @Test
    public void 메이팅찾기글_목록조회() throws Exception {
        // given
        String url = "/api/mating";
        MatingDto.ListRequest requestBody = MatingDto.ListRequest.builder()
                .lastMatingId(1L)
                .limit(10)
                .build();
        List<MatingReadResponseDto> responses = matingAppTestHelper.getTestReadResponseDtoList();
        doReturn(responses).when(matingReadRepository).readList(anyLong(), anyInt());
        // when
        ResultActions resultActions = matingMockMvcHelper.submitGetWithRequestBody(url, requestBody);
        List<MatingReadResponseDto> results = gson.fromJson(resultActions.andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeToken<List<MatingReadResponseDto>>(){}.getType());


        // then
        resultActions.andExpect(status().isOk());
        assertThat(results.size()).isNotEqualTo(0);
        assertThat(results.get(results.size()-1).getId()).isEqualTo(2);
    }
}
