package com.fitmate.account.mapper;

import com.fitmate.account.dto.AccountDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.vo.Password;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountDtoMapper {
    AccountDtoMapper INSTANCE = Mappers.getMapper(AccountDtoMapper.class);
    @Mapping(target = "password", source = "password", qualifiedByName = "wrapPassword")
    Account toEntity(AccountDto.JoinRequest joinRequest);

    @Named("wrapPassword")
    default Password wrapPassword(String password) {
        return Password.builder()
                .value(password)
                .build();
    }

    @Mapping(target = "password", source = "password", qualifiedByName = "extractPassword")
    AccountDto.JoinResponse toResponse(Account account);

    @Named("extractPassword")
    default String extractPassword(Password password) {
        return password.getValue();
    }
}
