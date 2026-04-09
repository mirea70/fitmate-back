package com.fitmate.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ActiveProfiles("test")
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected static final Long TEST_ACCOUNT_ID = 1L;

    protected AccountJpaEntity createTestAccountEntity() {
        return AccountJpaEntity.builder()
                .id(TEST_ACCOUNT_ID)
                .loginName("testUser")
                .password("encoded")
                .nickName("테스터")
                .introduction("소개")
                .name("홍길동")
                .phone("01012345678")
                .email("test@test.com")
                .birthDate(LocalDate.of(2000, 1, 1))
                .gender("MALE")
                .role("USER")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
