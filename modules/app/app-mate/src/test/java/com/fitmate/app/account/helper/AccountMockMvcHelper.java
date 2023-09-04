package com.fitmate.app.account.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.mate.exceptions.GlobalExceptionHandler;
import com.fitmate.app.mate.mating.dto.MatingDto;
import com.google.gson.Gson;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

public class AccountMockMvcHelper {
    private final MockMvc mockMvc;

    private final Gson gson;

    public AccountMockMvcHelper(Object target) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(target).build();
        this.gson = new Gson();
    }
    public AccountMockMvcHelper(Object target, GlobalExceptionHandler globalExceptionHandler) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(globalExceptionHandler)
                .build();
        this.gson = new Gson();
    }

    public ResultActions submitPost(AccountDto.JoinRequest joinRequest, String url) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(joinRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    public ResultActions submitMultiPart(AccountDto.JoinRequest joinRequest, String url) throws Exception {

        MockMultipartFile convertedCreateDto = new MockMultipartFile("joinRequest", null, "application/json", gson.toJson(joinRequest).getBytes(StandardCharsets.UTF_8));

        return mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST ,url)
                        .file((MockMultipartFile) joinRequest.getProfileImage())
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
}
