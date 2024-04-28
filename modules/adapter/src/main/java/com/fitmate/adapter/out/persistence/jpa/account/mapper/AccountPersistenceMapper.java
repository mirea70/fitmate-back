package com.fitmate.adapter.out.persistence.jpa.account.mapper;

import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import com.fitmate.domain.account.*;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import org.springframework.stereotype.Component;

@Component
public class AccountPersistenceMapper {
    public Account entityToDomain(AccountJpaEntity account) {

        AccountId id = new AccountId(account.getId());
        Password password = new Password(account.getPassword());
        ProfileInfo profileInfo = new ProfileInfo(account.getNickName(), account.getIntroduction(), account.getProfileImageId());
        PrivateInfo privateInfo = new PrivateInfo(account.getName(), account.getPhone(), account.getEmail());
        Gender gender = Gender.valueOf(account.getGender());
        AccountRole role = AccountRole.valueOf(account.getRole());

        return Account.withId(
                id,
                account.getLoginName(),
                password,
                profileInfo,
                privateInfo,
                gender,
                role,
                account.getCreatedAt(),
                account.getUpdatedAt(),
                account.getDeletedAt(),
                account.getFollowings(),
                account.getFollowers()
                );
    }

    public AccountJpaEntity domainToEntity(Account account) {

        Long id = account.getId() != null ? account.getId().getValue() : null;
        ProfileInfo profileInfo = account.getProfileInfo();
        PrivateInfo privateInfo = account.getPrivateInfo();

        return new AccountJpaEntity(
                id,
                account.getLoginName(),
                account.getPassword().getValue(),
                profileInfo.getNickName(),
                profileInfo.getIntroduction(),
                profileInfo.getProfileImageId(),
                privateInfo.getName(),
                privateInfo.getPhone(),
                privateInfo.getEmail(),
                account.getGender().name(),
                account.getRole().name(),
                account.getFollowings(),
                account.getFollowers(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }
}
