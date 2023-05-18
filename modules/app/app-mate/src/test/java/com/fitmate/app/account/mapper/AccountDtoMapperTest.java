package com.fitmate.app.account.mapper;

import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.app.account.helper.AccountAppTestHelper;
import com.fitmate.app.mate.account.mapper.AccountDtoMapper;
import com.fitmate.domain.account.dto.AccountDuplicateCheckDto;
import com.fitmate.domain.account.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AccountDtoMapperTest {
    @Autowired
    AccountDtoMapper accountDtoMapper;
    @Autowired
    AccountAppTestHelper accountAppTestHelper;

    @Test
    public void toEntity테스트 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        // when
        Account result = accountDtoMapper.toEntity(joinRequest);
        // then
        assertEquals(joinRequest.getPrivateInfo().getEmail(), result.getEmail());
    }

    @Test
    public void toResponse테스트 () throws Exception {
        // given
        Account account = accountAppTestHelper.getTestAccount();
        // when
        AccountDto.JoinResponse result = accountDtoMapper.toResponse(account);
        // then
        assertEquals(account.getEmail(), result.getPrivateInfo().getEmail());
    }

    @Test
    public void toDuplicatedCheckDto테스트 () throws Exception {
        // given
        AccountDto.JoinRequest joinRequest = accountAppTestHelper.getTestAccountJoinRequest();
        // when
        AccountDuplicateCheckDto result = accountDtoMapper.toDuplicatedCheckDto(joinRequest);
        // then
        assertEquals(joinRequest.getPrivateInfo().getName(), result.getName());
        assertEquals(joinRequest.getPrivateInfo().getEmail(), result.getEmail());
        assertEquals(joinRequest.getPrivateInfo().getPhone(), result.getPhone());
        assertEquals(joinRequest.getProfileInfo().getNickName(), result.getNickName());
    }
}
