package com.fitmate.app.mating.controller;


import com.fitmate.app.account.helper.FileTestHelper;
import com.fitmate.app.mate.exceptions.GlobalExceptionHandler;
import com.fitmate.app.mate.mating.controller.MatingRequestController;
import com.fitmate.app.mating.helper.MatingAppTestHelper;
import com.fitmate.app.mating.helper.MatingMockMvcHelper;
import com.fitmate.config.GsonUtil;
import com.fitmate.domain.mating.domain.repository.MatingReadRepository;
import com.fitmate.domain.mating.dto.MatingQuestionDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MatingRequestControllerTest {

    @InjectMocks
    private MatingRequestController target;

    @Mock
    private MatingReadRepository matingReadRepository;

    private MatingAppTestHelper matingAppTestHelper;

    private MatingMockMvcHelper matingMockMvcHelper;

    private Gson gson;

    @BeforeEach
    public void init() {
        matingAppTestHelper = new MatingAppTestHelper();
        matingMockMvcHelper = new MatingMockMvcHelper(target, new GlobalExceptionHandler());
        gson = GsonUtil.buildGson();
    }

    @Test
    public void 질문화면_조회 () throws Exception {
        // given
        String url = "/api/mating/1/request/question";
        MatingQuestionDto.Response response = matingAppTestHelper.getTestQuestionResponseDto();
        doReturn(response).when(matingReadRepository).readQuestion(anyLong());
        // when
        ResultActions resultActions = matingMockMvcHelper.submitGet(url);
        MatingQuestionDto.Response result = gson.fromJson(resultActions.andReturn()
                .getResponse().getContentAsString(StandardCharsets.UTF_8), MatingQuestionDto.Response.class);
        // then
        resultActions.andExpect(status().isOk());
        assertThat(result.getComeQuestion()).isEqualTo("몇살이세요?");
    }
}
