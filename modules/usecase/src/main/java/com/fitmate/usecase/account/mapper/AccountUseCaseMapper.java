package com.fitmate.usecase.account.mapper;

import com.fitmate.domain.account.Account;
import com.fitmate.domain.account.Password;
import com.fitmate.domain.account.PrivateInfo;
import com.fitmate.domain.account.ProfileInfo;
import com.fitmate.domain.account.enums.AccountRole;
import com.fitmate.domain.account.enums.Gender;
import com.fitmate.port.in.account.command.AccountJoinCommand;
import com.fitmate.port.out.account.AccountProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountUseCaseMapper {

    private final BCryptPasswordEncoder passwordEncoder;

    public AccountProfileResponse domainToResponse(Account account) {

        ProfileInfo profileInfo = account.getProfileInfo();
        PrivateInfo privateInfo = account.getPrivateInfo();

        return new AccountProfileResponse(
                account.getId().getValue(),
                account.getLoginName(),
                profileInfo.getNickName(),
                profileInfo.getIntroduction(),
                profileInfo.getProfileImageId(),
                privateInfo.getName(),
                privateInfo.getPhone(),
                privateInfo.getEmail(),
                privateInfo.getBirthDate(),
                account.getRole().name(),
                account.getGender().name(),
                account.getFollowings(),
                account.getFollowers()
        );
    }

    public Account commandToDomain(AccountJoinCommand joinCommand) {

        Password password = new Password(passwordEncoder.encode(joinCommand.password()));
        ProfileInfo profileInfo = new ProfileInfo(joinCommand.nickName(), joinCommand.introduction(), null);
        PrivateInfo privateInfo = new PrivateInfo(joinCommand.name(), joinCommand.phone(), joinCommand.email(), joinCommand.birthDate());
        Gender gender = Gender.valueOf(joinCommand.gender().name());
        AccountRole role = AccountRole.valueOf(joinCommand.role().name());

        return Account.withoutId(
                joinCommand.loginName(),
                password,
                profileInfo,
                privateInfo,
                gender,
                role,
                null,
                null,
                null);
    }
}
