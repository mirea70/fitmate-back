package com.fitmate.system.security.config;

import com.fitmate.system.security.error.JwtAccessDeniedHandler;
import com.fitmate.system.security.error.JwtAuthenticationEntryPoint;
import com.fitmate.system.security.error.TokenExpiredFilter;
import com.fitmate.system.security.filter.JwtAuthenticationFilter;
import com.fitmate.system.security.filter.JwtAuthorizationFilter;
import com.fitmate.system.security.handler.JwtLogoutHandler;
import com.fitmate.system.security.provider.TokenProvider;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtLogoutHandler jwtLogoutHandler;
    private final TokenExpiredFilter tokenExpiredFilter;

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
                .antMatchers("/").permitAll()
                .antMatchers(swaggerAuthMatchers()).permitAll()
                .antMatchers("/api/accounts/join").permitAll()
                .antMatchers("/api/mate/check").permitAll()
                .antMatchers("/api/chat/check").permitAll()
                .antMatchers("/api/**").authenticated()
//                .antMatchers("/api/**").permitAll()
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
                    .addFilter(new JwtAuthenticationFilter(authenticationManager, tokenProvider))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, tokenProvider));
        }
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
}
