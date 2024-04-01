package com.fitmate.app.mate.account.mapper;

import com.fitmate.app.mate.account.dto.AccountDto;
import com.fitmate.domain.account.dto.AccountDataDto;
import com.fitmate.domain.account.dto.AccountDuplicateCheckDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.vo.Password;
import org.mapstruct.*;
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

    AccountDto.Response toResponse(Account account);

    @Mapping(target = "password", source = "password", qualifiedByName = "extractPassword")
    AccountDto.JoinResponse toJoinResponse(Account account);

    @Named("extractPassword")
    default String extractPassword(Password password) {
        return password.getValue();
    }

    @Mapping(target = "name", source = "privateInfo.name")
    @Mapping(target = "email", source = "privateInfo.email")
    @Mapping(target = "phone", source = "privateInfo.phone")
    @Mapping(target = "nickName", source = "profileInfo.nickName")
    AccountDuplicateCheckDto toDuplicatedCheckDto(AccountDto.JoinRequest joinRequest);

    AccountDto.Response toRealResponse(AccountDataDto.Response dataResponse);
}
