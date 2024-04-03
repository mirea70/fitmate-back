package com.fitmate.app.chat.config;

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
                .info(getInfo());
    }

    private Info getInfo() {
        return new Info()
                .title("Fitmate Mate Service API")
                .description("""
                        Fitmate 앱의 Chat 서비스 API 문서입니다.

                        **[참고]**: 요청이나 응답 DTO의 세부 규칙/설명을 확인하고 싶을 경우 각 Body의 Schema를 확인해주세요.
                        
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
