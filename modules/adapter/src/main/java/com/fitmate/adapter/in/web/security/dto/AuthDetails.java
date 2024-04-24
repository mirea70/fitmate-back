package com.fitmate.adapter.in.web.security.dto;

import com.fitmate.adapter.out.persistence.jpa.account.entity.AccountJpaEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
public class AuthDetails implements UserDetails {
    private AccountJpaEntity account;
    private Map<String, Object> attributes;

    // 일반 로그인
    public AuthDetails(AccountJpaEntity account) {
        this.account = account;
    }
    // OAuth2 로그인
    public AuthDetails(AccountJpaEntity account, Map<String, Object> attributes) {
        this.account = account;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> String.valueOf(account.getRole()));
        return authorities;
    }

    //    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        member.getRoleList().forEach(n -> {
//            authorities.add(() -> n);
//        });
//        return authorities;
//    }
//    @Override
//    public Map<String, Object> getAttributes() {
//        return attributes;
//    }

//    @Override
//    public String getName() {return (String) attributes.get("name");}

    public String getEmail() {return account.getEmail();}

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getLoginName();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
