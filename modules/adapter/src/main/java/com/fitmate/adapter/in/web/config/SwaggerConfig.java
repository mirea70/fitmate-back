package com.fitmate.adapter.in.web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(getInfo())
                .addSecurityItem(getSecurityRequirement())
                .components(getComPonents());
    }

    private Info getInfo() {
        return new Info()
                .title("Fitmate API Docs")
                .description("""
                        Fitmate 앱의 API 문서입니다.

                        **[참고1]**: 요청 DTO의 세부 규칙/설명을 확인하고 싶을 경우 각 Body의 Schema를 확인해주세요.
                        
                        **[참고2]**: 회원가입을 제외한 모든 API는 Access Token이 필요합니다.
                        
                        login-endpoint를 통해 Access 토큰을 발급받은 후, 우측 상단의 Authorize 버튼을 눌러 "Bearer "를 제외한 토큰 값을 넣어주세요.
                        """)
                .version("0.0.1");
    }

    private Components getComPonents() {
        return  new Components()
                .addSecuritySchemes("Bearer-Key", getSecurityScheme());
    }

    private SecurityRequirement getSecurityRequirement() {
        return new SecurityRequirement().addList("Bearer-Key");
    }

    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT");
    }

}
