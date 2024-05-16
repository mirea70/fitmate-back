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
                account.getRole().name(),
                account.getGender().name(),
                account.getFollowings(),
                account.getFollowers()
        );
    }

    public Account commandToDomain(AccountJoinCommand joinCommand) {

        Password password = new Password(passwordEncoder.encode(joinCommand.getPassword()));
        ProfileInfo profileInfo = new ProfileInfo(joinCommand.getNickName(), joinCommand.getIntroduction(), null);
        PrivateInfo privateInfo = new PrivateInfo(joinCommand.getName(), joinCommand.getPhone(), joinCommand.getEmail());
        Gender gender = Gender.valueOf(joinCommand.getGender().name());
        AccountRole role = AccountRole.valueOf(joinCommand.getRole().name());

        return Account.withoutId(
                joinCommand.getLoginName(),
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
