package com.fitmate.app.account.helper;

import com.fitmate.app.account.dto.AccountDto;
import com.fitmate.app.exceptions.GlobalExceptionHandler;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class JoinMockMvcHelper {
    private MockMvc mockMvc;

    private Gson gson;

    public ResultActions submitPost(AccountDto.JoinRequest joinRequest, String url) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(joinRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    public JoinMockMvcHelper(Object target) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(target).build();
        this.gson = new Gson();
    }
    public JoinMockMvcHelper(Object target, GlobalExceptionHandler globalExceptionHandler) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(globalExceptionHandler)
                .build();
        this.gson = new Gson();
    }
}
