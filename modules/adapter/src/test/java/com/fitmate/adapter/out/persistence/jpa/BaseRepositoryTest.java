package com.fitmate.adapter.out.persistence.jpa;

import com.fitmate.adapter.out.persistence.config.QueryDslConfig;
import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
public abstract class BaseRepositoryTest {

    protected AccountJpaEntity createAccountEntity(String suffix) {
        return AccountJpaEntity.builder()
                .loginName("login" + suffix)
                .password("@Aa111111")
                .nickName("닉네임" + suffix)
                .introduction("소개글")
                .name("이름" + suffix)
                .email(suffix + "@test.com")
                .phone("010" + String.format("%08d", suffix.hashCode() & 0x7FFFFFFF).substring(0, 8))
                .birthDate(LocalDate.of(2000, 1, 1))
                .gender("MALE")
                .role("USER")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
