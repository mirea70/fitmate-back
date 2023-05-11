package com.fitmate.app.account.mapper;

import com.fitmate.app.account.dto.AccountDto;
import com.fitmate.domain.account.dto.AccountDuplicateCheckDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.entity.vo.Password;
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

    @Mapping(target = "name", source = "privateInfo.name")
    @Mapping(target = "email", source = "privateInfo.email")
    @Mapping(target = "phone", source = "privateInfo.phone")
    @Mapping(target = "nickName", source = "profileInfo.nickName")
    AccountDuplicateCheckDto toDuplicatedCheckDto(AccountDto.JoinRequest joinRequest);
}
