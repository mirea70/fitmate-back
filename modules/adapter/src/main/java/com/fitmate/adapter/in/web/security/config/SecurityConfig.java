package com.fitmate.adapter.in.web.security.config;

import com.fitmate.adapter.in.web.security.error.CustomAuthenticationFailureHandler;
import com.fitmate.adapter.in.web.security.error.JwtAccessDeniedHandler;
import com.fitmate.adapter.in.web.security.error.JwtAuthenticationEntryPoint;
import com.fitmate.adapter.in.web.security.error.TokenExpiredFilter;
import com.fitmate.adapter.in.web.security.filter.JwtAuthenticationFilter;
import com.fitmate.adapter.in.web.security.filter.JwtAuthorizationFilter;
import com.fitmate.adapter.in.web.security.handler.JwtLogoutHandler;
import com.fitmate.adapter.in.web.security.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtLogoutHandler jwtLogoutHandler;
    private final TokenExpiredFilter tokenExpiredFilter;
    private final CustomAuthenticationFailureHandler failureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .headers().frameOptions().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().disable()
                .apply(new JwtLogin())
                .and()
                .addFilterBefore(tokenExpiredFilter, JwtAuthorizationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers(swaggerAuthMatchers()).permitAll()
                .antMatchers(excludeApi()).permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .and()
                .logout()
                .addLogoutHandler(jwtLogoutHandler)
                .logoutSuccessUrl("/");
        return httpSecurity.build();
    }

    public class JwtLogin extends AbstractHttpConfigurer<JwtLogin, HttpSecurity> {
        @Override
        public void configure(HttpSecurity httpSecurity) throws Exception {
            AuthenticationManager authenticationManager = httpSecurity.getSharedObject(AuthenticationManager.class);
            httpSecurity
                    .addFilterBefore(new JwtAuthenticationFilter("/login",
                            authenticationManager,
                            tokenProvider,
                            failureHandler), UsernamePasswordAuthenticationFilter.class)
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, tokenProvider));
        }
    }

//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    private String[] swaggerAuthMatchers(){
        return new String[]{
                "/api-docs",
                "/swagger",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/api-docs/**",
                "/swagger-ui/index.html"
        };
    }

    private String[] excludeApi() {
        return new String[]{
                "/api/account/join",
                "/api/account/check/phone",
                "/api/account/check/loginName",
                "/api/sms/request/code",
                "/api/sms/check/code",
                "/api/auth/refresh",
        };
    }
}
