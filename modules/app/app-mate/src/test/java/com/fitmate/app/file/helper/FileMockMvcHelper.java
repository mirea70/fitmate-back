package com.fitmate.app.file.helper;

import com.fitmate.app.mate.exceptions.GlobalExceptionHandler;
import com.google.gson.Gson;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class FileMockMvcHelper {
    private final MockMvc mockMvc;

    private final Gson gson;


    public FileMockMvcHelper(Object target, GlobalExceptionHandler globalExceptionHandler) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(target)
                .setControllerAdvice(globalExceptionHandler)
                .build();
        this.gson = new Gson();
    }

    public ResultActions submitGet(String url) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );
    }
}
