package com.fitmate.app.mating.controller;


import com.fitmate.app.config.annotation.WithMockCustomUser;
import com.fitmate.app.config.factory.WithMockCustomUserSecurityContextFactory;
import com.fitmate.app.mate.exceptions.GlobalExceptionHandler;
import com.fitmate.app.mate.mating.controller.MatingRequestController;
import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.service.MatingRequestService;
import com.fitmate.app.mating.helper.MatingAppTestHelper;
import com.fitmate.app.mating.helper.MatingMockMvcHelper;
import com.fitmate.config.GsonUtil;
import com.fitmate.domain.account.repository.AccountRepository;
import com.fitmate.domain.mating.mate.domain.repository.MatingReadRepository;
import com.fitmate.domain.mating.mate.dto.MatingQuestionDto;
import com.fitmate.system.security.config.SecurityConfig;
import com.fitmate.system.security.dto.AuthDetails;
import com.fitmate.system.security.service.AuthDetailsService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@WebAppConfiguration
@Import(WithMockCustomUserSecurityContextFactory.class)
public class MatingRequestControllerTest {

    @Autowired
    private MatingRequestController target;

    @MockBean
    private MatingReadRepository matingReadRepository;

    @MockBean
    private MatingRequestService matingRequestService;

    private MatingAppTestHelper matingAppTestHelper;

    private MatingMockMvcHelper matingMockMvcHelper;

    private Gson gson;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() {
        matingAppTestHelper = new MatingAppTestHelper();
        matingMockMvcHelper = new MatingMockMvcHelper(target, new GlobalExceptionHandler());
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
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

    @Test
    @WithMockCustomUser
    public void 메이트신청 () throws Exception {
        // given
        String url = "/api/mating/3/request";
        doReturn(12L).when(matingRequestService).matingRequest(any(MatingDto.Apply.class));
        MatingDto.Apply applyDto = MatingDto.Apply.builder()
                .comeAnswer("5글자에요")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(applyDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(status().isOk());
    }
}
