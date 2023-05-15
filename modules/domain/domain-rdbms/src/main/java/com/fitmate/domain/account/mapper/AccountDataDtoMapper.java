package com.fitmate.domain.account.mapper;

import com.fitmate.domain.account.dto.AccountDataDto;
import com.fitmate.domain.account.entity.Account;
import com.fitmate.domain.account.entity.vo.Password;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountDataDtoMapper {
    AccountDataDtoMapper INSTANCE = Mappers.getMapper(AccountDataDtoMapper.class);

    @Mapping(target = "password", source = "password", qualifiedByName = "extractPassword")
    AccountDataDto.Response toResponse(Account account);

    @Named("extractPassword")
    default String extractPassword(Password password) {
        return password.getValue();
    }
}
