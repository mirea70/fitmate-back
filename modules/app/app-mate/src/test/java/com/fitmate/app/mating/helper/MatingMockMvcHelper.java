package com.fitmate.app.mating.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitmate.app.mate.exceptions.GlobalExceptionHandler;
import com.fitmate.app.mate.mating.dto.MatingDto;
import com.fitmate.config.GsonUtil;
import com.google.gson.Gson;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

public class MatingMockMvcHelper {
    private final MockMvc mockMvc;

    private final Gson gson = GsonUtil.buildGson();

    public ResultActions submitPost(MatingDto.Create createDto, String url) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(createDto))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)

//                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    public ResultActions submitMultiPart(MatingDto.Create createDto, String url, MockMultipartFile mockFile) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        MockMultipartFile convertedCreateDto = new MockMultipartFile("createDto", null, "application/json", gson.toJson(createDto).getBytes(StandardCharsets.UTF_8));

        return mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST ,url)
                        .file(mockFile)
                        .file(convertedCreateDto)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
        );
    }

    public ResultActions submitGet(String url) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.get(url)
//                        .queryParam(key, value)
////                        .param(key, value)
        );
    }

    public MatingMockMvcHelper(Object target) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(target).build();
    }
    public MatingMockMvcHelper(Object target, GlobalExceptionHandler globalExceptionHandler) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

}
