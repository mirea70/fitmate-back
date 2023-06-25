package com.fitmate.app.mating.controller;

import com.fitmate.app.account.helper.FileTestHelper;
import com.fitmate.app.mate.mating.controller.MatingController;
import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.app.mate.mating.service.MatingService;
import com.fitmate.app.mating.helper.MatingAppTestHelper;
import com.fitmate.app.mating.helper.MatingMockMvcHelper;
import com.fitmate.domain.mating.entity.Mating;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MatingControllerTest {
    @InjectMocks
    private MatingController target;
    @Mock
    private MatingService matingService;

    private MatingAppTestHelper matingAppTestHelper;

    private MatingMockMvcHelper matingMockMvcHelper;

    private FileTestHelper fileTestHelper;

    private Gson gson;

    @BeforeEach
    public void init() {
        matingAppTestHelper = new MatingAppTestHelper();
        matingMockMvcHelper = new MatingMockMvcHelper(target);
        fileTestHelper = new FileTestHelper();
        gson = new Gson();
    }

    @Test
    public void 메이팅찾기글_등록_성공 () throws Exception {
        // given
        final String url = "/api/mating";
        MatingDto.Create requestDto = matingAppTestHelper.getTestRequest();
        MockMultipartFile mockMultipartFile = fileTestHelper.getMockMultipartFile("image");
        MatingDto.Response responseDto = matingAppTestHelper.getTestResponse();
        doReturn(responseDto).when(matingService).register(any(MatingDto.Create.class));
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
}
